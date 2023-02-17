package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    Optional<Card> findById(Long card_id);
    List<Card> findAllByNameContains(String cardName);

    List<Card> findAllByCompanyContains(String cardCompany);





}
