package com.example.community.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/swagger")
public class SwaggerRedirector {
    @GetMapping(value = "/view")
    public ResponseEntity<?> swagger(){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/swagger-ui/index.html"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
