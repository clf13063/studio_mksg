package com.example.studio_mksg.repository.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
//    @JoinColumn(name = "category_id", insertable = false, updatable = false)  // 外部キー
    @JoinColumn(name = "category_id")
    private Category category;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", updatable = true)
    private LocalDateTime updatedDate;

    @Version
    private Integer version;
}
