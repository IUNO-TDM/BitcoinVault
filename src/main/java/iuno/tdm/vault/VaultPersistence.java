package iuno.tdm.vault;

import org.bitcoinj.core.Context;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by goergch on 24.05.17.
 */
public class VaultPersistence {
    private static VaultPersistence instance;
    private  Connection connection;
    private static final String PREFIX = "Vault";

    private static final Logger logger = LoggerFactory.getLogger(VaultPersistence.class);
    private VaultPersistence() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try{
            String workDir = System.getProperty("user.home") + "/." + PREFIX;
            connection = DriverManager.getConnection("jdbc:sqlite:" + workDir + "/database.db");
//            connection = DriverManager.getConnection("jbdc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS  `wallets` (`walletId` TEXT NOT NULL, " +
                    "`userId` TEXT NOT NULL, " +
                    "`walletFile` TEXT NOT NULL," +
                    "PRIMARY KEY(`walletId`));");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static VaultPersistence getInstance() throws ClassNotFoundException {
        if (VaultPersistence.instance == null) {
            VaultPersistence.instance = new VaultPersistence();
        }
        return VaultPersistence.instance;
    }

    public void addWallet(UserWallet userWallet) {
        String walletId = userWallet.getId().toString();
        String userId = userWallet.getUserId();
        String walletFileName = userWallet.getWalletFile().toString();

        try{
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate(String.format("insert into wallets values('%s','%s','%s')",
                    walletId,
                    userId,
                    walletFileName));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteWallet(UserWallet userWallet){
        String walletId = userWallet.getId().toString();
        try{
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate(String.format("delete from wallets where walletId = '%s'",
                    walletId));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public UserWallet[] recoverWallets(Context context, PeerGroup peerGroup) {
        ArrayList<UserWallet> userWallets = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from wallets");
            while(rs.next()){
                String walletId = rs.getString("walletId");
                String userId = rs.getString("userId");
                String walletFileName = rs.getString("walletFile");
                try {
                    userWallets.add(new UserWallet(walletId,userId,context,walletFileName, peerGroup));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userWallets.toArray(new UserWallet[userWallets.size()]);
    }


    @Override
    protected void finalize() throws Throwable {
        try
        {
            if(connection != null)
                connection.close();
        }
        catch(SQLException e)
        {
            // connection close failed.
            System.err.println(e);
        }
        super.finalize();
    }
}
