package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.ChangeBenefitReqDTO;
import com.example.cardmonkey.dto.PasswordDTO;
import com.example.cardmonkey.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 비밀번호 변경
     */
    @PostMapping("/changePassword/{userId}")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호를 확인 후 변경합니다.")
    public String changePassword(@PathVariable String userId, @RequestBody PasswordDTO dto) {
        // TODO: 로직 이동
        if (dto.getCurrentPassword() == null) {
            return "현재 비밀번호를 입력해주세요";}
        if (dto.getNewPassword() == null) {
            return "새로운 비밀번호를 입력해주세요";}
        if (memberService.updatePassword(userId, dto.getCurrentPassword(), dto.getNewPassword()) == null) {
            return "현재 비밀번호가 일치하지 않습니다.";}
        if (dto.getCurrentPassword().equals(dto.getNewPassword())) {
            return "현재 비밀번호와 동일합니다.";
        } else {
            return "비밀번호가 변경 되었습니다.";
        }
    }

    /**
     * 혜택 변경
     */
    @PatchMapping("/changeBenefit/{userId}")
    @ApiOperation(value = "혜택 변경", notes = "회원가입시 선택했던 3가지의 혜택을 수정합니다.")
    public String changeBenefit(@PathVariable String userId, @RequestBody ChangeBenefitReqDTO req) {
        memberService.changeBenefit(userId, req);
        return "혜택변경 완료";
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/deleteAccount/{userId}")
    @ApiOperation(value = "회원 탈퇴", notes = "회원을 탈퇴합니다.")
    public String deleteAccount(@PathVariable String userId) {
        // TODO: 로직 이동
        try {
            memberService.deleteAccount(userId);
            return "회원탈퇴 완료";
        } catch (Exception e) {
            e.printStackTrace();
            return "회원탈퇴 실패";
        }
    }
}
