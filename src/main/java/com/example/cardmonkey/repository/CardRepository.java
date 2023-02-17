package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    Optional<Card> findById(Long card_id);

    Card findAllById(Long id);
}
