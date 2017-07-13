package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.WalletsApiService;
import io.swagger.api.factories.WalletsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.Error;
import io.swagger.model.Payout;
import io.swagger.model.Transaction;
import java.util.UUID;
import io.swagger.model.UserId;

import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/wallets")
@Consumes({ "application/json" })
@Produces({ "application/json", "text/plain" })
@io.swagger.annotations.Api(description = "the wallets API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-07-13T12:01:01.236Z")
public class WalletsApi  {
   private final WalletsApiService delegate = WalletsApiServiceFactory.getWalletsApi();

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "Creates a new wallet for a user", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "create:wallet", description = "Create a Wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "wallet created", response = String.class) })
    public Response addWallet(@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@ApiParam(value = "the userId" ) UserId userId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.addWallet(accessToken,userId,securityContext);
    }
    @POST
    @Path("/{walletId}/payouts")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "set PayoutAddress and iniate Payout", notes = "", response = Payout.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "create:payout", description = "create payout")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "payout succeessfully created", response = Payout.class),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "wrong authCode or not authorized", response = Payout.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = Payout.class) })
    public Response createPayoutForWallet(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "the payoutAddress" ,required=true) Payout payout
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.createPayoutForWallet(walletId,payout,accessToken,securityContext);
    }
    @DELETE
    @Path("/{walletId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "delete a user wallet", notes = "", response = void.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "delete:wallet", description = "Delete a Wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = void.class) })
    public Response deleteWallet(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteWallet(walletId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/credit")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get current credit for wallet", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:credit", description = "readout the credit for wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getCredit(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getCredit(walletId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/newaddress")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get new receive address", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "get:address", description = "get a fresh address for a wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "here is a new reveiving address", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getNewAddress(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getNewAddress(walletId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/payouts/{payoutId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "Get payout", notes = "", response = Payout.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:payouts", description = "get Payout information")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "payout object", response = Payout.class),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "wrong authCode or not authorized", response = Payout.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = Payout.class) })
    public Response getPayout(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "the payout ID",required=true) @PathParam("payoutId") UUID payoutId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPayout(walletId,payoutId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/payouts/{payoutId}/transactions")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "Get payout transaction(s)", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:payouts", description = "get Payout information")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "list of transactions", response = Transaction.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "wrong authCode or not authorized", response = Transaction.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = Transaction.class, responseContainer = "List") })
    public Response getPayoutTransactions(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "the payout ID",required=true) @PathParam("payoutId") UUID payoutId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPayoutTransactions(walletId,payoutId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/payouts")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "returns payout IDs", notes = "", response = UUID.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:payouts", description = "get Payout information")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "payout IDs", response = UUID.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "wrong authCode or not authorized", response = UUID.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = UUID.class, responseContainer = "List") })
    public Response getPayoutsForWalletId(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPayoutsForWalletId(walletId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/publicSeed")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get the Public Seed of the keychain", notes = "", response = String.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:publicseed", description = "get the publicseed for wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "here is the public seed", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getPublicSeed(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPublicSeed(walletId,accessToken,securityContext);
    }
    @GET
    @Path("/{walletId}/transactions")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get txs with utxo for wallet", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:credit", description = "readout the credit for wallet")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Transaction.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = Transaction.class, responseContainer = "List") })
    public Response getTransactions(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getTransactions(walletId,accessToken,securityContext);
    }
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "returns wallet IDs for userID", notes = "", response = String.class, responseContainer = "List", authorizations = {
        @io.swagger.annotations.Authorization(value = "Vault_Auth", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "read:wallet", description = "Read Wallet information")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "wallet IDs for UserID", response = String.class, responseContainer = "List") })
    public Response getWalletId(@ApiParam(value = "the users ID the wallet is created for",required=true) @QueryParam("userId") String userId
,@ApiParam(value = "oauth2 accessToken",required=true) @QueryParam("accessToken") String accessToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getWalletId(userId,accessToken,securityContext);
    }
}
