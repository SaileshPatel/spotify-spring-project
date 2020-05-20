package com.spotify.project.authorisation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SpotifyAuthorisation {

    private String clientId;
    private String clientSecret;

    private SpotifyApi api;
    private URI redirectURL = SpotifyHttpManager.makeUri("http://localhost:8080/code/spotify");
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    public SpotifyAuthorisation(@Value("${spotify.clientId}") String clientId, @Value("${spotify.clientSecret}") String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        api = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURL)
                .build();
    }

    public URI authorisationCodeUri(){
        authorizationCodeUriRequest = api.authorizationCodeUri().build();
        return authorizationCodeUriRequest.execute();
    }

    public void authorisationCode(){
        System.out.println(clientId);
        System.out.println(clientSecret);


        String code = authorisationCodeUri().toString();
        AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(code).build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            api.setAccessToken(authorizationCodeCredentials.getAccessToken());
            api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void refreshAuthorisationCode(){
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = api.authorizationCodeRefresh().build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            api.setAccessToken(authorizationCodeCredentials.getAccessToken());
            api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

}
