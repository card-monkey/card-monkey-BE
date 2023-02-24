package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.*;
import com.example.cardmonkey.entity.Benefit;
import com.example.cardmonkey.entity.Member;
import com.example.cardmonkey.exception.NoSuchMemberException;
import com.example.cardmonkey.jwt.JwtProvider;
import com.example.cardmonkey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    public String join(SignupReqDTO req) {
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
     * 아이디 중복체크 (회원가입 시)
     */
    @Transactional(readOnly = true)
    public String userIdValidation(ValidationDTO req) {
        if (memberRepository.existsByUserId(req.getUserId())) {
            return "1";
        } else {
            return null;
        }
    }

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public LoginResDTO login(LoginReqDTO req) {
        Member findMember = memberRepository.findByUserId(req.getUserId()).orElseThrow(
                NoSuchMemberException::new);

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
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);

        if (!checkPassword(req.getCurrentPassword(), findMember.getPassword())) {
            return "현재 비밀번호가 일치하지 않습니다.";
        }
        findMember.updatePassword(encodingPassword(req.getNewPassword()));
        return "비밀번호가 변경 되었습니다.";
    }

    /**
     * 혜택 변경
     */
    public String changeBenefit(String userId, ChangeBenefitReqDTO req) {
        Member findMember = memberRepository.findByUserId(userId).orElseThrow(
                NoSuchMemberException::new);

        findMember.updateBenefit(new Benefit(req.getBenefit()));

        return "혜택변경 완료";
    }

    /**
     * 회원 탈퇴
     */
    public String deleteAccount(String userId) {
        memberRepository.deleteByUserId(userId);
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
    private boolean checkPassword(String inputPassword, String originPassword) {
        return passwordEncoder.matches(inputPassword, originPassword);
    }
}
