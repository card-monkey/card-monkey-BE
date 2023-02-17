package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {

}
