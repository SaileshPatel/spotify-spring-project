package com.spotify.project.controllers;

import com.spotify.project.authorisation.SpotifyAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    SpotifyAuth spotifyAuth;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("auth", spotifyAuth.getAuthorisationUri());
        return "index";
    }
}
