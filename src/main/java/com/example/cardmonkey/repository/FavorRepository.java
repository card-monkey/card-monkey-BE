package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import com.example.cardmonkey.entity.Favor;
import com.example.cardmonkey.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorRepository extends JpaRepository<Favor, Long> {

    Favor findAllByCardAndMember(Card card, Member member);

    List<Favor> findAllByMember(Member member);
}
