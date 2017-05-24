package io.swagger.api;

import io.swagger.model.*;
import io.swagger.api.WalletsApiService;
import io.swagger.api.factories.WalletsApiServiceFactory;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.swagger.model.Error;
import io.swagger.model.Transaction;
import java.util.UUID;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-05-23T15:06:19.418Z")
public class WalletsApi  {
   private final WalletsApiService delegate = WalletsApiServiceFactory.getWalletsApi();

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "Creates a new wallet for a user", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "wallet created", response = String.class) })
    public Response addWallet(@ApiParam(value = "the users ID the wallet is created for",required=true) @QueryParam("userId") String userId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.addWallet(userId,securityContext);
    }
    @DELETE
    @Path("/{walletId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "delete a user wallet", notes = "", response = void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = void.class) })
    public Response deleteWallet(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.deleteWallet(walletId,securityContext);
    }
    @GET
    @Path("/{walletId}/credit")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get current credit for wallet", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getCredit(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getCredit(walletId,securityContext);
    }
    @GET
    @Path("/{walletId}/lastaddress")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get last receive address", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "here is the last reveiving address", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getLastAddress(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getLastAddress(walletId,securityContext);
    }
    @GET
    @Path("/{walletId}/newaddress")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get new receive address", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "here is a new reveiving address", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getNewAddress(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getNewAddress(walletId,securityContext);
    }
    @GET
    @Path("/{walletId}/publicseed")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get the Public Seed of the keychain", notes = "", response = String.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "here is the public seed", response = String.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = String.class) })
    public Response getPublicSeed(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getPublicSeed(walletId,securityContext);
    }
    @GET
    @Path("/{walletId}/transactions")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "get txs with utxo for wallet", notes = "", response = Transaction.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK", response = Transaction.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = Transaction.class, responseContainer = "List") })
    public Response getTransactions(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getTransactions(walletId,securityContext);
    }
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "returns wallet IDs for userID", notes = "", response = String.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "wallet IDs for UserID", response = String.class, responseContainer = "List") })
    public Response getWalletId(@ApiParam(value = "the users ID the wallet is created for",required=true) @QueryParam("userId") String userId
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.getWalletId(userId,securityContext);
    }
    @PUT
    @Path("/{walletId}/payoutaddress")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    @io.swagger.annotations.ApiOperation(value = "set PayoutAddress and iniate Payout", notes = "", response = void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "payout succeeded", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "wrong authCode or not authorized", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "no wallet with this id", response = void.class) })
    public Response payoutCredit(@ApiParam(value = "the wallets ID",required=true) @PathParam("walletId") UUID walletId
,@ApiParam(value = "the payoutAddress",required=true) @QueryParam("payoutaddress") String payoutaddress
,@ApiParam(value = "the authToken to payout this",required=true) @QueryParam("authToken") String authToken
,@Context SecurityContext securityContext)
    throws NotFoundException {
        return delegate.payoutCredit(walletId,payoutaddress,authToken,securityContext);
    }
}
