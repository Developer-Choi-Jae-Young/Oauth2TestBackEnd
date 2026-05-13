package org.example.oauth2test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        String msg = "This is Main Controller Test Method";
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
}
