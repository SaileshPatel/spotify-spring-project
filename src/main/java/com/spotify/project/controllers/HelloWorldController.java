package com.spotify.project.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    String hello() {
        return "Hello World";
    }

}
