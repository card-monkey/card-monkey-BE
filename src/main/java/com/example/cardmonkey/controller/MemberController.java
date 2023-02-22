package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.ChangeBenefitReqDTO;
import com.example.cardmonkey.dto.LoginReqDTO;
import com.example.cardmonkey.dto.PasswordReqDTO;
import com.example.cardmonkey.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"회원 서비스"}, description = "회원 서비스를 담당합니다.")
public class MemberController {

    private final MemberService memberService;

    /**
     * 비밀번호 변경
     */
    @PatchMapping("/info/changePassword")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 확인 후 변경합니다.")
    public String changePassword(Authentication authentication, @RequestBody PasswordReqDTO req) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return memberService.updatePassword(userId, req);
    }

    /**
     * 혜택 변경
     */
    @PatchMapping("/info/changeBenefit")
    @ApiOperation(value = "혜택 변경", notes = "회원가입시 선택했던 3가지의 혜택을 수정합니다.")
    public String changeBenefit(Authentication authentication, @RequestBody ChangeBenefitReqDTO req) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return memberService.changeBenefit(userId, req);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/info/deleteAccount")
    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    public String deleteAccount(Authentication authentication) {
        LoginReqDTO loginReqDTO = (LoginReqDTO) authentication.getPrincipal();
        String userId = loginReqDTO.getUserId();
        return memberService.deleteAccount(userId);
    }
}
