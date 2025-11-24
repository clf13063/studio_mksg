package com.example.studio_mksg.controller.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartItem {
    private int id;
    private String name;
    private int price;   // int 型で使う
    private int quantity;
    private String image;

    private List<Integer> itemIds = new ArrayList<>();
    private List<String> itemImages = new ArrayList<>();
    private List<String> itemNames = new ArrayList<>();
    private List<BigDecimal> prices = new ArrayList<>();
    private List<Integer> quantities;
    private List<BigDecimal> subtotals = new ArrayList<>();
    private BigDecimal totalAmount;

    public int getSubTotal() {
        return price * quantity;
    }

    // BigDecimal → int に変換して保持
    public void setPrice(BigDecimal price) {
        this.price = price.intValue();
    }
}
