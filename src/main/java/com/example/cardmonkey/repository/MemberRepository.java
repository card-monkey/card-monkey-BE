package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);

    Boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
