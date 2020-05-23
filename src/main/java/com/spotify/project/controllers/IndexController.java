package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    SpotifyAuth spotifyAuth;

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
        return "auth";
    }
}
