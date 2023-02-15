package com.example.cardmonkey.service;

import com.example.cardmonkey.dto.LoginRequest;
import com.example.cardmonkey.dto.LoginResponse;
import com.example.cardmonkey.dto.SignupRequest;
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
    public String signup(SignupRequest req) {
        if (memberRepository.existsByUserId(req.getUserId())) {
            return req.getUserId() + "는 이미 존재하는 아이디 입니다.";
        }
        String encodingPassword = encodingPassword(req.getPassword());
        req.setPassword(encodingPassword);
        if (req.getRole() == null || req.getRole().equals("")) {
            req.setRole("ROLE_USER");
        }
        Member member = req.toEntity();
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
//        Member result = memberRepository.findById(member.getId()).orElseThrow(IllegalArgumentException::new);
        Member result = memberRepository.findByUserId(member.getUserId()).orElseThrow(IllegalArgumentException::new);
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
        Member member = memberRepository.findByUserId(memberId).orElseThrow(IllegalArgumentException::new);
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
}
