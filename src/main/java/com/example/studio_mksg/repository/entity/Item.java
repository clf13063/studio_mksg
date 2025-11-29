package com.example.studio_mksg.repository.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "items")
@Getter
@Setter
public class Item {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column
    private String image;

    @Column
    private int stock;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Integer  categoryId;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)  // 外部キー
    private Category category;

    @Column(name = "created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name = "updated_date", insertable = false, updatable = true)
    private Date updatedDate;
}
