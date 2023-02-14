package com.example.cardmonkey.service;

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
        return "회원가입이 완료";
    }

    /**
     * 로그인
     */
    public String login(){
        return null;
    }
    /* 예시코드 ===============================
    public String signin(UserDTO userDTO){
        Optional<User> loggedIn=userRepository.findByEmailAndPassword(userDTO.getEmail(),userDTO.getPassword());
        try{
            User user=loggedIn.get();
            return jwtProvider.token(user);
        }catch(NoSuchElementException e){
            return "failed";
        }
    }
     =============================== */

    /**
     * 비밀번호 변경
     */
    @Transactional
    public Member updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
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
    public void deleteAccount(Long id) {
        memberRepository.deleteById(id);
    }
}
