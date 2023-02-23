package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Paid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaidRepository extends JpaRepository<Paid, Long> {

    @Query(value = "select p from Paid p join fetch p.card where p.member.id = :memberId")
    List<Paid> findAllByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndCardId(Long memberId, Long cardId);

    void deleteByMemberIdAndCardId(Long memberId, Long cardId);
}
