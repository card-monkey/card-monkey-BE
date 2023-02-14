package com.example.cardmonkey.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private String role;

    @Embedded
    private Benefit benefit;

    @OneToMany(mappedBy = "member")
    private List<Favor> favors = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Paid> paids = new ArrayList<>();
}
