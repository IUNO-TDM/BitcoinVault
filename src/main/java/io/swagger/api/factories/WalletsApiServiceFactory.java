package io.swagger.api.factories;

import io.swagger.api.WalletsApiService;
import io.swagger.api.impl.WalletsApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-05-23T15:06:19.418Z")
public class WalletsApiServiceFactory {
    private final static WalletsApiService service = new WalletsApiServiceImpl();

    public static WalletsApiService getWalletsApi() {
        return service;
    }
}
