package com.example.studio_mksg.controller.form;

import com.example.studio_mksg.repository.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class SalesItemForm {

    private String id;
    private String name;
    private String price;
    private String image;
    private String stock;
    private String categoryId;
    private Category category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer version;
}