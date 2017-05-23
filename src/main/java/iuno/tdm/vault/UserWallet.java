package iuno.tdm.vault;

import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.Sensor;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by goergch on 23.05.17.
 */
public class UserWallet {

    private UUID id;
    private String userId;
    private Wallet wallet;
    private Context context;
    private static final String PREFIX = "Vault";
    private static final Logger logger = LoggerFactory.getLogger(UserWallet.class);

    public UserWallet(String userId, Context context) throws IOException {
        id = UUID.randomUUID();
        this.userId = userId;
        this.context = context;
        wallet = new Wallet(context);
        String workDir = System.getProperty("user.home") + "/." + PREFIX;
        new File(workDir).mkdirs();
        File walletFile = new File(workDir, PREFIX + id +".wallet");
        try {
            wallet.autosaveToFile(walletFile, 5, TimeUnit.SECONDS, null).saveNow();
        } catch (IOException e) {
            logger.error(String.format("creating wallet file failed: %s", e.getMessage()));
            throw new IOException(String.format("creating wallet file failed: %s", e.getMessage()));
        }

    }

    public UUID getId(){
        return id;
    }

    public String getUserId(){
        return userId;
    }

    public Coin getBalance(){
        return wallet.getBalance();
    }

    public Wallet getWallet(){
        return wallet;
    }

    public String getFreshAddress(){
        return wallet.freshReceiveAddress().toBase58();
    }

    public String getLastAddress(){
        return wallet.currentReceiveAddress().toBase58();
    }

    public void payoutCredit(Address payoutAddress){
        SendRequest sendRequest = SendRequest.emptyWallet(payoutAddress);
        try {
            wallet.completeTx(sendRequest);
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
        }
    }
}
