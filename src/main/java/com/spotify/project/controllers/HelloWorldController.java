package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuthorisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    @Autowired
    SpotifyAuthorisation spotifyAuthorisation;

    @RequestMapping("/")
    String hello() {
        spotifyAuthorisation = new SpotifyAuthorisation();

        spotifyAuthorisation.authorisationCode();

        return "Hello World";
    }

}
