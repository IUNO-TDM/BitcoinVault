package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.Error;
import io.swagger.model.Transaction;
import java.util.UUID;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-05-23T15:06:19.418Z")
public abstract class WalletsApiService {
    public abstract Response addWallet( @NotNull String userId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteWallet(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getCredit(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getLastAddress(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getNewAddress(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getPublicSeed(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getTransactions(UUID walletId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getWalletId( @NotNull String userId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response payoutCredit(UUID walletId, @NotNull String payoutaddress, @NotNull String authToken,SecurityContext securityContext) throws NotFoundException;
}
