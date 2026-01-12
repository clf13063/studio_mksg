package com.example.studio_mksg.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRegisterForm {

    private String name;
    private String price;
    private String stock;
    private String image;
    private Integer categoryId;
}
