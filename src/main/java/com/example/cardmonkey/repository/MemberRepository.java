package com.example.cardmonkey.repository;

import com.example.cardmonkey.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUserIdAndPassword(String userId, String Password);

    Member findByName(String name);
}
