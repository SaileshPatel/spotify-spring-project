package com.spotify.project.authorisation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class SpotifyAuth {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    private String authCode;
    private SpotifyApi api;

    private URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080/code/spotify");

    public SpotifyAuth() {
        api = new SpotifyApi.Builder()
                .setClientId(getClientId())
                .setClientSecret(getClientSecret())
                .setRedirectUri(redirectURI)
                .build();
    }

    public URI getAuthorisationUri()
    {
        AuthorizationCodeUriRequest authUrlCode = api.authorizationCodeUri()
                .client_id(getClientId())
                .response_type("code")
                .show_dialog(true)
                .build();

        return authUrlCode.execute();
    }

    public void getTokens(String code) {
        AuthorizationCodeRequest codeRequest = api.authorizationCode(code).build();
        try {
            AuthorizationCodeCredentials authCredentials = codeRequest.execute();
            api.setAccessToken(authCredentials.getAccessToken());
            api.setRefreshToken((authCredentials.getRefreshToken()));
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void refreshTokens() {
        AuthorizationCodeRefreshRequest authRefresh = api.authorizationCodeRefresh().build();
        try{
            AuthorizationCodeCredentials authCredentials = authRefresh.execute();
            api.setAccessToken(authCredentials.getAccessToken());
            api.setRefreshToken(authCredentials.getRefreshToken());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
