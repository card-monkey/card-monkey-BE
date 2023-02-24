package com.example.cardmonkey.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="token_blacklist")
public class Token {

    @Id
    @Column(name="blocked_token")
    private String token;
}
