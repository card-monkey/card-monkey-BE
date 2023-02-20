package com.example.cardmonkey.service;

import com.example.cardmonkey.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public boolean isTokenExists(String token) {
        return tokenRepository.existsByToken(token);
    }
}
