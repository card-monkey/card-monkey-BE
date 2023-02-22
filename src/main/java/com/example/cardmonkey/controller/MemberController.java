package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.ChangeBenefitReqDTO;
import com.example.cardmonkey.dto.PasswordReqDTO;
import com.example.cardmonkey.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"회원 서비스"}, description = "회원 서비스를 담당합니다.")
public class MemberController {

    private final MemberService memberService;

    /**
     * 비밀번호 변경
     */
    @PostMapping("/changePassword/{userId}")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 확인 후 변경합니다.")
    public String changePassword(@PathVariable String userId, @RequestBody PasswordReqDTO req) {
        return memberService.updatePassword(userId, req);
    }

    /**
     * 혜택 변경
     */
    @PatchMapping("/changeBenefit/{userId}")
    @ApiOperation(value = "혜택 변경", notes = "회원가입시 선택했던 3가지의 혜택을 수정합니다.")
    public String changeBenefit(@PathVariable String userId, @RequestBody ChangeBenefitReqDTO req) {
        return memberService.changeBenefit(userId, req);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/deleteAccount/{userId}")
    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    public String deleteAccount(@PathVariable String userId) {
        return memberService.deleteAccount(userId);
    }
}
