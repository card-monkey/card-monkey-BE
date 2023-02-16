package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByNameContains(String cardName);
}
