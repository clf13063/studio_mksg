package com.example.studio_mksg.service;

import com.example.studio_mksg.controller.form.ItemForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;


    public List<ItemForm> findAllItem(String selectCategory) {

        List<Item> results;
        //カテゴリ指定されていない場合は全件取得
        if (selectCategory == null || selectCategory.isEmpty()) {
            results = itemRepository.findAll();
            return setItemForm(results);
        //カテゴリが指定されている場合
        } else {
            try {
                Integer categoryId = Integer.parseInt(selectCategory);
                results = itemRepository.findByCategoryId(categoryId);
            } catch (NumberFormatException e) {
                results = itemRepository.findAll();
            }
            results = itemRepository.findByCategoryId(Integer.parseInt(selectCategory));
            return setItemForm(results);
        }
    }

    private List<ItemForm> setItemForm(List<Item> results) {
        List<ItemForm> items = new ArrayList<>();

        for (Item result : results) {
            ItemForm itemForm = new ItemForm();
            itemForm.setName(result.getName());
            itemForm.setPrice(String.valueOf(result.getPrice()));
            itemForm.setImage(result.getImage());
            itemForm.setStock(String.valueOf(result.getStock()));
            itemForm.setCategory_id(String.valueOf(result.getCategoryId()));
            items.add(itemForm);
        }
        return items;
    }
}