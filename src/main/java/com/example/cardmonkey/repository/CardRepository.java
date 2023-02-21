package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    Optional<Card> findById(Long card_id);

    List<Card> findAllByNameContains(String cardName);

    Card findAllById(Long id);

    List<Card> findAllByCompanyContains(String cardCompany);
}
