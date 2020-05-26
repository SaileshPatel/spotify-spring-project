package com.spotify.project.authorisation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
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
                .scope("user-top-read")
                .show_dialog(true)
                .build();

        return authUrlCode.execute();
    }


    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public URI getRedirectURI() { return redirectURI; }

    public void setAccessToken(String accessToken) {
        api.setAccessToken(accessToken);
    }

    public void  setRefreshToken(String refreshToken) {
        api.setRefreshToken(refreshToken);
    }

    public GetUsersTopTracksRequest getTopTracks() {
        return api.getUsersTopTracks().build();
    }
}
