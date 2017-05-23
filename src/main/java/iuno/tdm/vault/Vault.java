package iuno.tdm.vault;

import ch.qos.logback.classic.Level;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Vault(){
        BriefLogFormatter.initWithSilentBitcoinJ();
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel("info"));

        // Context.enableStrictMode();
        final NetworkParameters params = TestNet3Params.get();
        context = new Context(params);
        Context.propagate(context);
    }

    public static Vault getInstance(){
        if(Vault.instance == null){
            Vault.instance = new Vault();
        }
        return Vault.instance;

    }

    public UUID createWallet(String userId) throws IOException {
        UserWallet wallet = new UserWallet(userId, context);
        blockChain.addWallet(wallet.getWallet());
        peerGroup.addWallet(wallet.getWallet());
        userWallets.put(wallet.getId(),wallet);
        return wallet.getId();
    }

    public void deleteWallet(UUID walletId) throws NullPointerException{
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("Cannot delete wallet with id "
                    + walletId.toString() + ", it doesn't exists");
        }
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

    public String getLastAddress(UUID walletId){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        return userWallets.get(walletId).getLastAddress();
    }

    public void payoutCredit(UUID walletId, String payoutAddress){
        if(!userWallets.containsKey(walletId)){
            throw new NullPointerException("no wallet with id " + walletId.toString());
        }
        userWallets.get(walletId).payoutCredit(Address.fromBase58(context.getParams(),payoutAddress));
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

    }

    public void stop(){
        peerGroup.stop();
    }



}
