package com.spotify.project.controllers;

import com.google.gson.Gson;
import com.spotify.project.authorisation.SpotifyAuth;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

@Controller
public class IndexController {

    @Autowired
    SpotifyAuth spotifyAuth;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("auth", spotifyAuth.getAuthorisationUri());
        return "index";
    }

    @GetMapping("/code/spotify")
    public String authCode(@RequestParam(required = false) String code,
                           @RequestParam(required = false) String state,
                           @RequestParam(required = false) String error,
                           Model model) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<String, String>();

        bodyParams.add("client_id", spotifyAuth.getClientId());
        bodyParams.add("client_secret", spotifyAuth.getClientSecret());
        bodyParams.add("grant_type", "authorization_code");
        bodyParams.add("code", code);
        bodyParams.add("redirect_uri", spotifyAuth.getRedirectURI().toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyParams, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://accounts.spotify.com/api/token",
                request, String.class);

        Map<String, String> jsonResponse = spotifyTokens(response);

        spotifyAuth.setAccessToken(jsonResponse.get("access_token"));
        spotifyAuth.setRefreshToken(jsonResponse.get("refresh_token"));

        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyAuth.getApi().getUsersTopTracks().build();

        try {
            Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            model.addAttribute("tracks", trackPaging.getItems());
        } catch (Exception e) {
            System.out.println(e.toString());
            model.addAttribute("error", e.toString());
        }
        

        // access_token, token_type, expires_in, refresh_token, scope


        return "auth";
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private Map<String, String> spotifyTokens(ResponseEntity<String> response) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Gson gson = new Gson();

        Map<String, String> map = gson.fromJson(response.getBody(), type);

        return map;
    }

}
