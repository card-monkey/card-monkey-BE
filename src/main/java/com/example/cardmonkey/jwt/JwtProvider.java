package com.example.cardmonkey.jwt;

import com.example.cardmonkey.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    //토큰을 발급하거나, 토큰을 유저 객체로 바꾸는 클래스

    public String token(Member member){
        Date date=new Date();

        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setIssuer("james")
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime()+3600000))
                .claim("userId",member.getUserId())
                .signWith(SignatureAlgorithm.HS256,"12345")
                .compact();

    }

    public Claims tokenToUser(String token){ // "Bearer sdmleeweawlek.ekwekwekewrkj.enwerkjwerknrwekn"

        if(token!=null) {
            token = token.substring("Bearer".length());

            return Jwts.parser()
                    .setSigningKey("12345")
                    .parseClaimsJws(token)
                    .getBody();

        }else{
            return null;
        }

    }

}
