package io.swagger.api.impl;

import io.swagger.api.*;

import io.swagger.model.Error;
import io.swagger.model.Transaction;

import java.io.IOException;
import java.util.UUID;

import io.swagger.api.NotFoundException;

import iuno.tdm.vault.Vault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-05-23T15:06:19.418Z")
public class WalletsApiServiceImpl extends WalletsApiService {
    private static final Logger logger = LoggerFactory.getLogger(WalletsApiServiceImpl.class);
    @Override
    public Response addWallet( @NotNull String userId, SecurityContext securityContext) throws NotFoundException {

        Response resp;
        Error err = new Error();
        err.setMessage("success");
        UUID walletId = null;
        try {
            walletId = Vault.getInstance().createWallet(userId);

            resp = Response.status(201).entity(walletId.toString()).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Created new wallet %s", walletId));
        } catch (IOException e) {
            err.setMessage(e.getMessage());
            resp = Response.status(500).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response deleteWallet(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            Vault.getInstance().deleteWallet(walletId);
            resp = Response.status(200).entity("wallet deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Deleted wallet %s", walletId));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getCredit(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            long credit = Vault.getInstance().getCredit(walletId);
            resp = Response.status(200).entity(String.format("%d",credit)).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Got credit wallet %s:%d", walletId, credit));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getLastAddress(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            String address = Vault.getInstance().getLastAddress(walletId);
            resp = Response.status(200).entity(address).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Last receive address for wallet %s: %s", walletId, address));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getNewAddress(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            String address = Vault.getInstance().getFreshAddress(walletId);
            resp = Response.status(200).entity(address).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Fresh receive address for wallet %s: %s", walletId, address));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getPublicSeed(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            String seed = Vault.getInstance().getPublicSeed(walletId);
            resp = Response.status(200).entity(seed).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Public seed for wallet %s: %s", walletId, seed));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getTransactions(UUID walletId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            Transaction[] transactions = Vault.getInstance().getTransactions(walletId);
            resp = Response.status(200).entity(transactions).build();
//            logger.info(String.format("Public seed for wallet %s: %s", walletId, transactions));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
    @Override
    public Response getWalletId( @NotNull String userId, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        UUID[] ids = Vault.getInstance().getWalletIds(userId);
        String[] stringIds = new String[ids.length];
        for(int i = 0; i<ids.length; i++ ){
            stringIds[i] = ids[i].toString();
        }
        resp = Response.status(200).entity(stringIds).build();
        return resp;
    }
    @Override
    public Response payoutCredit(UUID walletId,  @NotNull String payoutaddress,  @NotNull String authToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");
        try{
            String txHash = Vault.getInstance().payoutCredit(walletId,payoutaddress, authToken);
            resp = Response.status(200).entity(txHash).type(MediaType.TEXT_PLAIN_TYPE).build();
            logger.info(String.format("Wallet emptied %s emptied", walletId));
        }catch (NullPointerException e){
            err.setMessage("no wallet found for id " + walletId);
            resp = Response.status(404).entity(err).build();
        }
        return resp;
    }
}
