package com.example.studio_mksg.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ItemUpdateForm {
    private String id;
    private String name;
    private String price;
    private String stock;
    private String image;
    private Integer categoryId;
    // 画像アップロード用
    private MultipartFile imageFile;
    // 画像変更なし用
    private String existingImage;
    private Integer version;
}
