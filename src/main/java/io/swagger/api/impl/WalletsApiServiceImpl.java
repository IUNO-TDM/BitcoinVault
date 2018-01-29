package io.swagger.api.impl;

import io.swagger.api.*;

import io.swagger.model.*;

import java.io.IOException;
import java.util.UUID;

import io.swagger.api.NotFoundException;

import io.swagger.model.Error;
import iuno.tdm.vault.AccessRule;
import iuno.tdm.vault.OAuthValidator;
import iuno.tdm.vault.Scope;
import iuno.tdm.vault.Vault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.validation.constraints.*;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2018-01-24T14:05:55.521Z")
public class WalletsApiServiceImpl extends WalletsApiService {
    private static final OAuthValidator VALIDATOR = new OAuthValidator("oauthHost");
    private static final Logger logger = LoggerFactory.getLogger(WalletsApiServiceImpl.class);

    @Override
    public Response addWallet(@NotNull String accessToken, UserId userId, SecurityContext securityContext) throws NotFoundException {

        Response resp;
        Error err = new Error();
        err.setMessage("success");
        UUID walletId = null;
        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            AccessRule accessRule = new AccessRule(null, new Scope[]{Scope.simple("write:wallet")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    walletId = Vault.getInstance().createWallet(userId.getUserId());

                    resp = Response.status(201).entity(walletId.toString()).type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Created new wallet %s", walletId));
                } catch (IOException e) {
                    err.setMessage(e.getMessage());
                    resp = Response.status(500).entity(err).build();
                }
            }
        }
        return resp;
    }


    @Override
    public Response createPayoutForWallet(UUID walletId, Payout payout, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {

        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.parameter("create:payout",
                                    new String[]{payout.getPayoutAddress(), payout.getAmount().toString()})});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    payout = Vault.getInstance().addPayoutForWallet(payout, walletId);
                    resp = Response.status(201).entity(payout).build();
                    logger.info(String.format("Created new payout %s", payout.getPayoutId()));
                } catch (IllegalArgumentException e) {
                    err.setMessage(e.getMessage());
                    resp = Response.status(409).entity(err).build();
                } catch (Exception e) {
                    err.setMessage(e.getMessage());
                    resp = Response.status(500).entity(err).build();
                }


            }

        }

        return resp;
    }

    @Override
    public Response deleteWallet(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"}, //TODO Check if admin is necessary
                            new Scope[]{Scope.simple("delete:wallet")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    Vault.getInstance().deleteWallet(walletId);
                    resp = Response.status(200).entity("wallet deleted").type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Deleted wallet %s", walletId));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }

        }

        return resp;
    }

    @Override
    public Response getConfirmedCredit(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:credit")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {

                try {
                    long credit = Vault.getInstance().getConfirmedCredit(walletId);
                    resp = Response.status(200).entity(String.format("%d", credit)).type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Got confirmed credit wallet %s:%d", walletId, credit));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }

        }

        return resp;

    }

    @Override
    public Response getCredit(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:credit")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {

                try {
                    long credit = Vault.getInstance().getCredit(walletId);
                    resp = Response.status(200).entity(String.format("%d", credit)).type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Got credit wallet %s:%d", walletId, credit));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }

        }

        return resp;

    }

    @Override
    public Response getNewAddress(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("get:address")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {

                try {
                    String address = Vault.getInstance().getFreshAddress(walletId);
                    resp = Response.status(200).entity(address).type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Fresh receive address for wallet %s: %s", walletId, address));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }

        }

        return resp;
    }

    @Override
    public Response getPayout(UUID walletId, UUID payoutId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:payouts")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                Payout payout = Vault.getInstance().getPayoutForIdAndWallet(payoutId, walletId);
                resp = Response.status(201).entity(walletId.toString()).entity(payout).build();
            }

        }

        return resp;
    }

    @Override
    public Response getPayoutTransactions(UUID walletId, UUID payoutId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:payouts")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                Transaction[] transactions = Vault.getInstance().getTransactionsForPayoutAndWallet(payoutId, walletId);
                resp = Response.status(201).entity(walletId.toString()).entity(transactions).build();
            }

        }

        return resp;
    }

    @Override
    public Response getPayoutsForWalletId(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:payouts")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                UUID[] payouts = Vault.getInstance().getPayoutIdsForWallet(walletId);
                resp = Response.status(201).entity(walletId.toString()).entity(payouts).build();
            }

        }

        return resp;
    }

    @Override
    public Response getPublicSeed(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:publicseed")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    String seed = Vault.getInstance().getPublicSeed(walletId);
                    resp = Response.status(200).entity(seed).type(MediaType.TEXT_PLAIN_TYPE).build();
                    logger.info(String.format("Public seed for wallet %s: %s", walletId, seed));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }
        }

        return resp;
    }

    @Override
    public Response getTransactions(UUID walletId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:credit")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    Transaction[] transactions = Vault.getInstance().getTransactions(walletId);
                    resp = Response.status(200).entity(transactions).build();
//            logger.info(String.format("Public seed for wallet %s: %s", walletId, transactions));
                } catch (NullPointerException e) {
                    err.setMessage("no wallet found for id " + walletId);
                    resp = Response.status(404).entity(err).build();
                }
            }
        }

        return resp;
    }

    @Override
    public Response getWalletId(@NotNull String userId, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.simple("read:wallet")});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                UUID[] ids = Vault.getInstance().getWalletIds(userId);
                String[] stringIds = new String[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    stringIds[i] = ids[i].toString();
                }
                resp = Response.status(200).entity(ids).build();
            }
        }
        return resp;
    }

    @Override
    public Response checkPayoutForWallet(UUID walletId, Payout payout, @NotNull String accessToken, SecurityContext securityContext) throws NotFoundException {
        Response resp;
        Error err = new Error();
        err.setMessage("success");

        OAuthValidator.Validation validation = VALIDATOR.validateToken(accessToken);
        if (!validation.valid) {
            err.setMessage("Validation of AccessToken not successful");
            resp = Response.status(403).entity(err).build();
        } else {
            String userId = Vault.getInstance().getUserIdForWalletId(walletId);
            AccessRule accessRule =
                    new AccessRule(
                            new String[]{userId, "admin"},
                            new Scope[]{Scope.parameter("create:payout",
                                    new String[]{payout.getPayoutAddress(), payout.getAmount().toString()})});
            if (!accessRule.applyValidation(validation)) {
                err.setMessage("Wrong scope or user");
                resp = Response.status(403).entity(err).build();
            } else {
                try {
                    PayoutCheck payoutCheck = Vault.getInstance().checkPayoutForWallet(payout, walletId);

                    resp = Response.status(200).entity(payoutCheck).build();

                    logger.info(String.format("Created new payout %s", payout.getPayoutId()));
                } catch (Exception e) {
                    err.setMessage(e.getMessage());
                    resp = Response.status(500).entity(err).build();
                }
            }
        }

        return resp;
    }
}
