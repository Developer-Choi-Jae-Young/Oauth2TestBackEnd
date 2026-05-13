package org.example.oauth2test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao")
public class KakaoController {
//    @GetMapping("/auth-code")
//    public void loginForm(
//            @RequestParam(required = false) String code,
//            @RequestParam(required = false) String error,
//            @RequestParam(name = "error_description", required = false) String errorDescription,
//            @RequestParam(required = false) String state
//    ){
//        System.out.println("### kakao 인가 코드 요청");
//        System.out.println("code: " + code);
//        System.out.println("error: " + error);
//        System.out.println("errorDescription: " + errorDescription);
//        System.out.println("state: " + state);
//
//        String authCode = code;
//    }
}
