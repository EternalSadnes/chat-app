package com.eternal.chatapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
