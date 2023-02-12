package com.example.cardmonkey.entity;

import lombok.*;

import javax.persistence.*;

public class Card {

    private Long id;
    private String name;
    private String company;
    private String description;
    private String imageURL;
    private int lastMonthPaid;
    private int annualFee;
    private CardType cardType;
    private String applyURL;
}
