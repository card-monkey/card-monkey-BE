package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavorRepository extends JpaRepository<Favor, Long> {

    Favor findAllByCardAndMember(Card card, Member member);

    List<Favor> findAllByMember(Member member);

    @Query(value = "SELECT f.card_id, count(*) AS CNT " +
            "FROM favor f " +
            "WHERE f.status = 1 " +
            "GROUP BY f.card_id " +
            "ORDER BY CNT DESC LIMIT 3;", nativeQuery = true)
    List<Object[]> selectFavorByRank();
}
