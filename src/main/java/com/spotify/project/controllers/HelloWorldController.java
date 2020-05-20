package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuth;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    SpotifyAuth auth = new SpotifyAuth();

    @RequestMapping("/")
    String hello() {
        return "Hello World";
    }

}
