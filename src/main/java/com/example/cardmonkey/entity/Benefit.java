package com.example.cardmonkey.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public Benefit(List<String> benefits) {
        for (String benefit : benefits) {
            switch (benefit) {
                case "coffee":
                    this.coffee = "yes"; break;
                case "transportation":
                    this.transportation = "yes"; break;
                case "movie":
                    this.movie = "yes"; break;
                case "delivery":
                    this.delivery = "yes"; break;
                case "phone":
                    this.phone = "yes"; break;
                case "gas":
                    this.gas = "yes"; break;
                case "simplePayment":
                    this.simplePayment = "yes"; break;
                case "tax":
                    this.tax = "yes"; break;
                case "shopping":
                    this.shopping = "yes"; break;
            }
        }
    }

    public List<String> makeBenefitList() {
        List<String> list = new ArrayList<>();

        if (this.getCoffee() != null) list.add("coffee");
        if (this.getTransportation() != null) list.add("transportation");
        if (this.getMovie() != null) list.add("movie");
        if (this.getDelivery() != null) list.add("delivery");
        if (this.getPhone() != null) list.add("phone");
        if (this.getGas() != null) list.add("gas");
        if (this.getSimplePayment() != null) list.add("simplePayment");
        if (this.getTax() != null) list.add("tax");
        if (this.getShopping() != null) list.add("shopping");

        return list;
    }
}
