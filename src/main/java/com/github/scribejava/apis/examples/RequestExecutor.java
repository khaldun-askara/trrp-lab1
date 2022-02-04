package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.DropboxApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class RequestExecutor {
    private CompletableFuture<String> refreshTokenF = new CompletableFuture<>();
    private Future<OAuth2AccessToken> accessTokenF;
    private OAuth20Service service;

    RequestExecutor() throws IOException, URISyntaxException {
        CallbackServer cbs = new CallbackServer();
        cbs.start();
        service = new ServiceBuilder("a4zewvhbr5zgj33")
                .apiSecret("gcea1cuhv7u2w0t")
                .callback(cbs.getAuthUrl())
                .build(DropboxApi.instance());
        authenticate(cbs);
    }

    RequestExecutor(String refreshToken) throws IOException {
        CallbackServer cbs = new CallbackServer();
        cbs.start();
        service = new ServiceBuilder("a4zewvhbr5zgj33")
                .apiSecret("gcea1cuhv7u2w0t")
                .callback(cbs.getAuthUrl())
                .build(DropboxApi.instance());
        authenticate(refreshToken);
    }

    private void authenticate(String refreshToken) {
        accessTokenF = getRefreshToken(refreshToken).thenApply(token -> {
            try {
                return service.refreshAccessToken(token);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    private void authenticate(CallbackServer cbs) throws IOException, URISyntaxException {
        accessTokenF = cbs.getOAuth1VerifierCF().thenApply(code -> {
            try {
                return service.getAccessToken(code);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("token_access_type", "offline");
        final String authorizationUrl = service.createAuthorizationUrlBuilder()
                .additionalParams(additionalParams)
                .build();
        Desktop.getDesktop().browse(new URI(authorizationUrl));
    }

    Response execute(OAuthRequest request) throws ExecutionException, InterruptedException, IOException {
        service.signRequest(accessTokenF.get(), request);
        return service.execute(request);
    }

    public String getRefreshToken() throws ExecutionException, InterruptedException {
        String refreshToken = accessTokenF.get().getRefreshToken();
        System.out.println();
        System.out.println("accessToken:" + accessTokenF.get().getAccessToken());
        System.out.println("refreshToken: " + refreshToken);
        return refreshToken;
    }

    CompletableFuture<String> getRefreshToken(String refreshToken) {
        refreshTokenF.complete(refreshToken);
        return refreshTokenF;
    }
}
