package io.swagger.api;

import io.swagger.api.*;
import io.swagger.model.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import io.swagger.model.Error;
import io.swagger.model.Payout;
import io.swagger.model.Transaction;
import java.util.UUID;
import io.swagger.model.UserId;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-07-13T12:01:01.236Z")
public abstract class WalletsApiService {
    public abstract Response addWallet( @NotNull String accessToken,UserId userId,SecurityContext securityContext) throws NotFoundException;
    public abstract Response createPayoutForWallet(UUID walletId,Payout payout, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response deleteWallet(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getCredit(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getNewAddress(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getPayout(UUID walletId,UUID payoutId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getPayoutTransactions(UUID walletId,UUID payoutId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getPayoutsForWalletId(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getPublicSeed(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getTransactions(UUID walletId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
    public abstract Response getWalletId( @NotNull String userId, @NotNull String accessToken,SecurityContext securityContext) throws NotFoundException;
}
