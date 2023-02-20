package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;

import java.util.List;

public interface CardRepositoryCustom {

    List<Card> findAllByBenefit(String benefitName);

    Card recommendRandomCardByBenefit(String benefitName, int offset);

    long countByBenefit(String benefitName);
}
