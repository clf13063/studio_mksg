package com.example.studio_mksg.controller.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {
    private int id;
    private String name;
    private int price;   // int 型で使う
    private int quantity;
    private String image;

    public int getSubTotal() {
        return price * quantity;
    }

    // BigDecimal → int に変換して保持
    public void setPrice(BigDecimal price) {
        this.price = price.intValue();
    }
}
