package iuno.tdm.vault;

import ch.qos.logback.classic.Level;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import io.swagger.model.Payout;
import io.swagger.model.Transaction;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by goergch on 23.05.17.
 */
public class Vault {

    private static Vault instance;
    private Context context;
    private PeerGroup peerGroup = null;
    BlockChain blockChain;

    private static final String PREFIX = "Vault";

    private HashMap<UUID, UserWallet> userWallets = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(Vault.class);
    private VaultPersistence vaultPersistence;

    private Vault(){
        BriefLogFormatter.initWithSilentBitcoinJ();
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel("info"));

        // Context.enableStrictMode();
        final NetworkParameters params = TestNet3Params.get();
        context = new Context(params);
        Context.propagate(context);
        try {
            vaultPersistence = VaultPersistence.getInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }

    public static Vault getInstance(){
        if(Vault.instance == null){
            Vault.instance = new Vault();
        }
        return Vault.instance;

    }

    public UUID createWallet(String userId) throws IOException {
        UserWallet wallet = new UserWallet(userId, context, peerGroup);
        blockChain.addWallet(wallet.getWallet());
        peerGroup.addWallet(wallet.getWallet());
        userWallets.put(wallet.getId(),wallet);
        vaultPersistence.addWallet(wallet);
        return wallet.getId();
    }

    public void deleteWallet(UUID walletId) throws NullPointerException{
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("Cannot delete wallet with id "
                    + walletId.toString() + ", it doesn't exists");
        }
        UserWallet userWallet = userWallets.get(walletId);
        vaultPersistence.deleteWallet(userWallet);
        blockChain.removeWallet(userWallet.getWallet());
        peerGroup.removeWallet(userWallet.getWallet());
        userWallet.deleteWallet();
        userWallets.remove(walletId);
    }

    public long getCredit(UUID walletId) throws NullPointerException{
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getBalance().getValue();
    }

    public UUID[] getWalletIds(String userId){
        ArrayList<UUID> ids = new ArrayList<>();
        for (UserWallet wallet:userWallets.values()) {
            if (wallet.getUserId().equals(userId)){
                ids.add(wallet.getId());
            }
        }

        return ids.toArray(new UUID[ids.size()]);
    }

    public String getFreshAddress(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getFreshAddress();
    }


    public Payout addPayoutForWallet(Payout payout, UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).addPayout(payout);

    }

    public String getPublicSeed(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getPublicSeed();
    }

    public io.swagger.model.Transaction[] getTransactions(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        TransactionOutput[] transactionOutputs = userWallets.get(walletId).getTransactionOutputs();
        io.swagger.model.Transaction[] swaggerTxs = new io.swagger.model.Transaction[transactionOutputs.length];
        for(int i=0; i< swaggerTxs.length;i++){
            TransactionOutput txOut = transactionOutputs[i];
            io.swagger.model.Transaction sTx = new io.swagger.model.Transaction();
            sTx.setAmount((int)txOut.getValue().value);
            sTx.setTxid(txOut.getParentTransactionHash().toString());

            sTx.setState(Transaction.StateEnum.UNKNOWN);
            sTx.setDepthInBlocks(Integer.MIN_VALUE);
            TransactionConfidence confidence = txOut.getParentTransaction().getConfidence();
            if (confidence != null) {
                switch (confidence.getConfidenceType()) {
                    case BUILDING:
                        sTx.setState(Transaction.StateEnum.BUILDING);
                        sTx.setDepthInBlocks(confidence.getDepthInBlocks());
                        break;
                    case PENDING:
                        sTx.setState(Transaction.StateEnum.PENDING);
                        sTx.setDepthInBlocks(Integer.MIN_VALUE + confidence.numBroadcastPeers());
                        break;
                    case DEAD:
                        sTx.setState(Transaction.StateEnum.DEAD);
                        sTx.setDepthInBlocks(Integer.MIN_VALUE);
                        break;
                    case IN_CONFLICT:
                        sTx.setState(Transaction.StateEnum.CONFLICT);
                        sTx.setDepthInBlocks(Integer.MIN_VALUE);
                        break;
                    case UNKNOWN:
                    default:
                }
            }
            swaggerTxs[i] = sTx;
        }
        return swaggerTxs;
    }


    public void start(){
        String workDir = System.getProperty("user.home") + "/." + PREFIX;
        new File(workDir).mkdirs();

        File chainFile = new File(workDir, PREFIX + ".spvchain");


        try {
            blockChain = new BlockChain(context, new SPVBlockStore(context.getParams(), chainFile));
        } catch (BlockStoreException e) {
            e.printStackTrace();
            return;
        }
        peerGroup = new PeerGroup(context, blockChain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(context.getParams()));
        Futures.addCallback(peerGroup.startAsync(), new FutureCallback() {
                    @Override
                    public void onSuccess(@Nullable Object o) {
                        logger.info("peer group finished starting");
                        peerGroup.connectTo(new InetSocketAddress("tdm-payment.axoom.cloud", 18333)); // TODO make this configurable
                        peerGroup.startBlockChainDownload(new DownloadProgressTracker());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                }
        );


        UserWallet[] uws = vaultPersistence.recoverWallets(context);
        for (UserWallet userWallet:uws) {
            userWallets.put(userWallet.getId(),userWallet);
            blockChain.addWallet(userWallet.getWallet());
            peerGroup.addWallet(userWallet.getWallet());
        }

    }

    public String getUserIdForWalletId(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getUserId();
    }

    public Payout getPayoutForIdAndWallet(UUID payoutId, UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getPayout(payoutId);
    }

    public UUID[] getPayoutIdsForWallet(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getPayoutIDs();

    }

    public Transaction[] getTransactionsForPayoutAndWallet(UUID payoutId, UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getTransactionsForPayout(payoutId);
    }

    public void stop(){
        peerGroup.stop();
    }

}
