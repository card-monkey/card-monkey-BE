package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;

public interface CardRepositoryCustom {

    Card recommendRandomCardByBenefit(String benefitName, int offset);

    long countByBenefit(String benefitName);
}
