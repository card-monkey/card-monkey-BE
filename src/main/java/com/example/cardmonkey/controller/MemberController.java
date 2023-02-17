package com.example.cardmonkey.controller;

import com.example.cardmonkey.dto.ChangeBenefitReqDTO;
import com.example.cardmonkey.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 혜택 변경
     */
    @PatchMapping("/changeBenefit/{userId}")
    public String changeBenefit(@PathVariable String userId,
                                @RequestBody ChangeBenefitReqDTO req) {
        memberService.changeBenefit(userId, req);

        return "혜택 변경 완료";
    }
}
