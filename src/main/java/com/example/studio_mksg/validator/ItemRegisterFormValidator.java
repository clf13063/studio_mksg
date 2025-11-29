package com.example.studio_mksg.validator;

import com.example.studio_mksg.controller.form.ItemRegisterForm;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
@Getter
@Setter
public class ItemRegisterFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemRegisterForm.class.isAssignableFrom(clazz);
    }

    private boolean isBlankOrWidthSpace(String value) {
        return StringUtils.isBlank(value)
                || value.replaceAll("　", "").trim().isEmpty();
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemRegisterForm form = (ItemRegisterForm) target;

        // --- 商品名 ---
        if (isBlankOrWidthSpace(form.getName())) {
            errors.rejectValue("name", "required.name", "・商品名を入力してください");
        } else if (form.getName().length() > 100) {
            errors.rejectValue("name", "length.name", "・商品名は100文字以内で入力してください");
        }

        // --- 価格 ---
        String priceStr = form.getPrice();
        if (isBlankOrWidthSpace(priceStr)) {
            errors.rejectValue("price", "required.price", "・価格を入力してください");
        } else {
            try {
                BigDecimal price = new BigDecimal(priceStr);
                BigDecimal maxPrice = new BigDecimal("9999999999"); // 上限
                if (price.compareTo(BigDecimal.ONE) < 0 || price.compareTo(maxPrice) > 0) {
                    errors.rejectValue("price", "invalid.price", "・価格は1～9,999,999,999の範囲で数値を入力してください");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("price", "invalid.price", "・価格は1～9,999,999,999の範囲で数値を入力してください");
            }
        }

        // --- 在庫 ---
        String stockStr = form.getStock();
        if (isBlankOrWidthSpace(stockStr)) {
            errors.rejectValue("stock", "required.stock", "・在庫数を入力してください");
        } else {
            try {
                long stock = Long.parseLong(stockStr);
                long maxStock = 9999999999L; // 上限
                if (stock < 0 || stock > maxStock) {
                    errors.rejectValue("stock", "invalid.stock", "・在庫数は0～9,999,999,999の範囲で数値を入力してください");
                }
            } catch (NumberFormatException e) {
                errors.rejectValue("stock", "invalid.stock", "・在庫数は0～9,999,999,999の範囲で数値を入力してください");
            }
        }

        // --- カテゴリー ---
        if (form.getCategoryId() == null || form.getCategoryId().equals("")) {
            errors.rejectValue("categoryId", "required.categoryId", "・カテゴリーを選択してください");
        }

        // --- 画像 ---
        if (isBlankOrWidthSpace(form.getImage())) {
            errors.rejectValue("image", "required.image", "・商品画像を入力してください");
        }
    }
}
