package com.example.cardmonkey.service;

import com.example.cardmonkey.jwt.JwtProvider;
import com.example.cardmonkey.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public String signup(){
        return null;
    }
    /* 예시코드 ===============================
    public String signup(UserDTO userDTO){
        userRepository.save(userDTO.toEntity());
        return "success";
    }
     =============================== */

    public String signin(){
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

}
