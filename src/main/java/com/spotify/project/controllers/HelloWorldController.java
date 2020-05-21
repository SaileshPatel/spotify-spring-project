package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    @Autowired
    SpotifyAuth auth;

    @RequestMapping("/")
    String hello() {

        System.out.println(auth.getAuthorisationUri());

        return "Hello World";
    }

}
