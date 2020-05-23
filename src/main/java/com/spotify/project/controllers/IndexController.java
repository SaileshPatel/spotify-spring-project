package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuth;
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

import java.util.Arrays;
import java.util.HashMap;

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

        ResponseEntity<String> response = restTemplate.postForEntity("https://accounts.spotify.com/api/token", request, String.class);

        System.out.println(response.getBody());

        return "auth";
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
