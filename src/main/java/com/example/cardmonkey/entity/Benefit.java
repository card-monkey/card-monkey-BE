package com.example.cardmonkey.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Benefit {

    private String coffee;
    private String transportation;
    private String movie;
    private String delivery;
    private String phone;
    private String gas;
    private String simplePayment;
    private String tax;
    private String shopping;
}
