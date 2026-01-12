package com.example.studio_mksg.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    private String id;
    private String name;
    private String price;
    private String image;
    private Integer stock;
    private String categoryId;
}
