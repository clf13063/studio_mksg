package com.example.studio_mksg.controller.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ItemRegisterForm {

    private String name;
    private String price;
    private String stock;
    private String image;
    private Integer categoryId;
}
