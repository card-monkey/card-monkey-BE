package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    Optional<Card> findById(Long card_id);

    List<Card> findAllByNameContains(String cardName);

    Card findAllById(Long id);

    List<Card> findAllByCompanyContains(String cardCompany);

    @Query("select c from Card c where c.id in :cards")
    List<Card> findCardByIds(@Param("cards") Collection<Long> cardIds);
}
