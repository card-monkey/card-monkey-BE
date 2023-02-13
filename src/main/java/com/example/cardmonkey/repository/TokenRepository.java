package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,String> {

    boolean existsByToken(String token);

}
