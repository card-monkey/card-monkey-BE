package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.entity.Benefit;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.jwt.JwtProvider;
import com.example.cardmonkey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public String join(SignupReqDTO req) {
        if (req.getUserId() == null || req.getPassword() == null || req.getName() == null) {
            return "모든 값을 입력해주세요";
        }
        if (memberRepository.existsByUserId(req.getUserId())) {
            return req.getUserId() + "는 이미 존재하는 아이디 입니다.";
        }

        req.setPassword(encodingPassword(req.getPassword()));
        if (req.getRole() == null || req.getRole().equals("")) {
            req.setRole("ROLE_USER");
        }
        Member member = req.toEntity(req.getBenefit());

        memberRepository.save(member);
        return "회원가입 완료";
    }

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public LoginResDTO login(LoginReqDTO req) {
        if (req.getUserId() == null || req.getPassword() == null) {
            return new LoginResDTO("아이디와 비밀번호 모두 입력해주세요");
        }

        Member findMember = memberRepository.findByUserId(req.getUserId()).get();

        if (!checkPassword(req.getPassword(), findMember.getPassword())) {
            return new LoginResDTO("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String createdToken = jwtProvider.makeToken(findMember);
        return LoginResDTO.builder()
                .userId(findMember.getUserId())
                .name(findMember.getName())
                .role(findMember.getRole())
                .token(createdToken)
                .loginStatus("로그인 완료")
                .build();
    }

    /**
     * 비밀번호 변경
     */
    public String updatePassword(String userId, PasswordReqDTO req) {
        if (req.getCurrentPassword() == null || req.getNewPassword() == null) {
            return "모든 값을 입력해주세요";
        }
        if (req.getCurrentPassword().equals(req.getNewPassword())) {
            return "입력하신 두 비밀번호가 동일합니다.";
        }

        Member findMember = memberRepository.findByUserId(userId).get();
        if (!checkPassword(req.getCurrentPassword(), findMember.getPassword())) {
            return "현재 비밀번호가 일치하지 않습니다.";
        } else {
            findMember.updatePassword(encodingPassword(req.getNewPassword()));
            return "비밀번호가 변경 되었습니다.";
        }
    }

    /**
     * 혜택 변경
     */
    public String changeBenefit(String userId, ChangeBenefitReqDTO req) {
        Member findMember = memberRepository.findByUserId(userId).get();

        findMember.updateBenefit(new Benefit(req.getBenefit()));
        return "혜택변경 완료";
    }

    /**
     * 회원 탈퇴
     */
    public String deleteAccount(String id) {
        memberRepository.deleteByUserId(id);
        return "회원탈퇴 완료";
    }

    /**
     * 비밀번호 인코딩
     */
    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 비밀번호 일치여부 확인
     */
    private boolean checkPassword(String inputPassword, String memberPassword) {
        return passwordEncoder.matches(inputPassword, memberPassword);
    }
}
