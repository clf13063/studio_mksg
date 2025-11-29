package com.example.studio_mksg.service;

import com.example.studio_mksg.controller.form.ItemRegisterForm;
import com.example.studio_mksg.controller.form.SalesItemForm;
import com.example.studio_mksg.repository.CategoryRepository;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Category;
import com.example.studio_mksg.repository.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesItemService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    
    public List<SalesItemForm> findAllSalesItem(String selectCategory) {

        List<Item> results;
        //カテゴリ指定されていない場合は全件取得
        if (selectCategory == null || selectCategory.isEmpty()) {
            results = itemRepository.findAll();
            return setSalesItemForm(results);
            //カテゴリが指定されている場合
        } else {
            try {
                Integer categoryId = Integer.parseInt(selectCategory);
                results = itemRepository.findByCategoryId(categoryId);
            } catch (NumberFormatException e) {
                results = itemRepository.findAll();
            }
            results = itemRepository.findByCategoryId(Integer.parseInt(selectCategory));
            return setSalesItemForm(results);
        }
    }

    private List<SalesItemForm> setSalesItemForm(List<Item> results) {
        List<SalesItemForm> items = new ArrayList<>();

        for (Item result : results) {
            SalesItemForm salesItemForm = new SalesItemForm();
            salesItemForm.setId(String.valueOf(result.getId()));
            salesItemForm.setName(result.getName());
            salesItemForm.setPrice(String.valueOf(result.getPrice()));
            salesItemForm.setImage(result.getImage());
            salesItemForm.setStock(String.valueOf(result.getStock()));
            salesItemForm.setCategoryId(String.valueOf(result.getCategoryId()));
            salesItemForm.setCategory(result.getCategory());
            salesItemForm.setCreatedDate(result.getCreatedDate());
            salesItemForm.setUpdatedDate(result.getUpdatedDate());
            items.add(salesItemForm);
        }
        return items;
    }

    public void registerItem(ItemRegisterForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが存在しません"));
        Item item = new Item();
        item.setName(form.getName());
        item.setPrice(new BigDecimal(form.getPrice()));
        item.setStock(Integer.parseInt(form.getStock()));
        item.setCategoryId(category.getId());
        item.setCategory(category);
        item.setImage(form.getImage());

        itemRepository.save(item);
    }
}
