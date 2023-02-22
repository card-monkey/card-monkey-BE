package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class CardRepositoryCustomImpl implements CardRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Card> findAllByBenefit(String benefitName, int offset, int limit) {
        String sql = "select c from Card c where c.benefit.";
        String postSql = " = :yes";
        sql += benefitName;
        sql += postSql;

        return em.createQuery(sql, Card.class)
                .setParameter("yes", "yes")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Card recommendRandomCardByBenefit(String benefitName, int offset) {
        String sql = "select c from Card c where c.benefit.";
        String postSql = " = :yes";
        sql += benefitName;
        sql += postSql;

        return em.createQuery(sql, Card.class)
                .setParameter("yes", "yes")
                .setFirstResult(offset)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public long countByBenefit(String benefitName) {
        String sql = "select count(c) from Card c where c.benefit.";
        String postSql = " = :yes";
        sql += benefitName;
        sql += postSql;

        return em.createQuery(sql, Long.class)
                .setParameter("yes", "yes")
                .getSingleResult();
    }
}
