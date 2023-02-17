package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.ChangeBenefitReqDTO;
import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.LoginResponse;
import com.example.cardmonkey.dto.SignupReqDTO;
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
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public String signup(SignupReqDTO req) {
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
    @Transactional
    public LoginResponse login(LoginRequest req) {
        if(req.getUserId() == null || req.getPassword() == null) {
            return new LoginResponse("아이디와 비밀번호를 입력하세요.");
        }
        Member member = req.toEntity();
//        Member result = memberRepository.findByUserId(member.getUserId()).orElseThrow(IllegalArgumentException::new);
        Member result = memberRepository.findByUserId(member.getUserId());
        try {
            if (result == null || !passwordMustBeSame(req.getPassword(), result.getPassword())) {
                throw new IllegalArgumentException();
            }
            String token = jwtProvider.makeToken(result);
            LoginResponse loginResponse = new LoginResponse(result, token);
            return loginResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    /**
     * 비밀번호 일치여부 확인
     */
    private boolean passwordMustBeSame(String requestPassword, String password) {
        if (!passwordEncoder.matches(requestPassword, password)) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public Member updatePassword(String memberId, String currentPassword, String newPassword) {
//        Member member = memberRepository.findByUserId(memberId).orElseThrow(IllegalArgumentException::new);
        Member member = memberRepository.findByUserId(memberId);
        boolean isSuccess = passwordEncoder.matches(currentPassword, member.getPassword());
        if (isSuccess) {
            member.updatePassword(encodingPassword(newPassword));
            return memberRepository.save(member);
        }
        return null;
    }

    /**
     * 비밀번호 Encoding
     */
    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteAccount(String id) {
        memberRepository.deleteByUserId(id);
    }

    @Transactional
    public void changeBenefit(String userId, ChangeBenefitReqDTO req) {
        Member findMember = memberRepository.findByUserId(userId);

        findMember.updateBenefit(new Benefit(req.getBenefit()));
    }
}
