package com.example.cardmonkey.controller;

import com.example.cardmonkey.service.MemberService;
import com.example.cardmonkey.entity.Token;
import com.example.cardmonkey.repository.TokenRepository;
import com.example.cardmonkey.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenRepository tokenRepository;

    @PostMapping("/signup")
    public String signup(){
        return null;
    }

    /* 회원가입 예시코드 =======================
    @PostMapping("/sign-up")
    public String signUp(UserDTO addUserDto) {
        userService.signup(addUserDto);
        return "success";
    }
    ==================================== */

    @PostMapping("signin")
    public String signin(){
        return null;
    }

    /* 로그인 예시코드 =========================
    @PostMapping("/sign-in")
    public String signIn(UserDTO loginUserDto) {
        String loginUserResponse = userService.signin(loginUserDto);
        return loginUserResponse;
    }
    ==================================== */


    /* 권한확인 예시코드 ==========================
    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('USER')") // USER 권한만 호출 가능
    public String hello(@AuthenticationPrincipal UserDTO user) {
        return user.getEmail() + ", 안녕하세요!";
    }
    ==================================== */

    @PostMapping("/logout")
    public String logout(@RequestHeader(name="Authorization") String header){
        tokenRepository.save(Token.builder().token(header).build());
        return "success";
    }
}
