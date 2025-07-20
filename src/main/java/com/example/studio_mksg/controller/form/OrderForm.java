package com.example.studio_mksg.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    private String lastName;
    private String firstName;
    private String postcode;
    private String address;
    private String tel;
    private Boolean differentReceiver;
    private String receiverLastName;
    private String receiverFirstName;
    private String receiverPostcode;
    private String receiverAddress;
    private String receiverTel;
    private String paymentMethod;
    private String creditNumber;
    private String securityCode;
    private String expiryMonth;
    private String expiryYear;
    private String numberOfPayment;
    private String email;
    private String confirmationEmail;
    private String giftHope;
    private Boolean giftMassageFlag;
    private String giftMassage;

    private List<Integer> itemIds = new ArrayList<>();
    private List<String> itemImages = new ArrayList<>();
    private List<String> itemNames = new ArrayList<>();
    private List<BigDecimal> prices = new ArrayList<>();
    private List<Integer> quantities;
    private List<BigDecimal> subtotals = new ArrayList<>();
    private BigDecimal totalAmount;
}
