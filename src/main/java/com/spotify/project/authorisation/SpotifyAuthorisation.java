package com.spotify.project.authorisation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SpotifyAuthorisation {

    @Value("${spotify.clientId}")
    private String clientId;
    @Value("${spotify.clientSecret}")
    private String clientSecret;

    private SpotifyApi api;
    private URI redirectURL;
    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    public SpotifyAuthorisation(){
        redirectURL = SpotifyHttpManager.makeUri("http://localhost:8080/code/spotify");

        api = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURL)
                .build();

        authorizationCodeUriRequest = api.authorizationCodeUri().build();

    }

    public URI authorisationCodeUri(){
        return authorizationCodeUriRequest.execute();
    }

}
