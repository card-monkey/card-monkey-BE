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
public class Paid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paid_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "paid")
    private List<PaidCard> paidCards = new ArrayList<>();
}
