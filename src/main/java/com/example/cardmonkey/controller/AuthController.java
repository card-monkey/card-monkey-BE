package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.LoginResponse;
import com.example.cardmonkey.dto.SignupReqDTO;
import com.example.cardmonkey.entity.Token;
import com.example.cardmonkey.repository.TokenRepository;
import com.example.cardmonkey.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"인증 서비스"}, description = "인증 서비스를 담당합니다.")
public class AuthController {

    private final MemberService memberService;
    private final TokenRepository tokenRepository;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입을 수행합니다.")
    public String signUp(@RequestBody SignupReqDTO req) {
        return memberService.signup(req);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인을 수행합니다.")
    public LoginResponse signIn(@RequestBody LoginRequest req) {
        return memberService.login(req);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃을 수행합니다.")
    public String logout(@RequestHeader(name="Authorization") String header) {
        tokenRepository.save(Token.builder().token(header).build());
        return "로그아웃 완료";
    }
}
