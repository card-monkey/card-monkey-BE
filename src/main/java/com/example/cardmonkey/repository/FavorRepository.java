package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Favor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavorRepository extends JpaRepository<Favor, Long> {

    @Query(value = "select f from Favor f join fetch f.card where f.member.id = :memberId")
    List<Favor> findAllByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndCardId(Long memberId, Long cardId);

    void deleteByMemberIdAndCardId(Long memberId, Long cardId);

    @Query(value =
            "SELECT f.card_id, count(*) as CNT " +
            "FROM favor f " +
            "GROUP BY f.card_id " +
            "ORDER BY CNT DESC LIMIT 5", nativeQuery = true)
    List<Object[]> selectFavorByRank();
}
