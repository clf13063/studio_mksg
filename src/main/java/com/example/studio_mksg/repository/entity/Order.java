package com.example.studio_mksg.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String postcode;

    @Column
    private String address;

    @Column
    private String tel;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_postcode")
    private String receiverPostcode;

    @Column(name = "receiver_address")
    private String receiverAddress;

    @Column(name = "receiver_tel")
    private String receiverTel;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column
    private String email;

    @Column(name = "gift_hope")
    private String giftHope;

    @Column(name = "gift_massage")
    private String giftMassage;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
