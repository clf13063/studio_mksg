package com.example.studio_mksg.service;

import com.example.studio_mksg.controller.form.ItemForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;


    public List<ItemForm> findAllItem(String selectCategory) {

        List<Item> results;
        //カテゴリ指定されていない場合は全件取得
        if (selectCategory == null || selectCategory.isEmpty()) {
            results = itemRepository.findAllByOrderByCreatedDateDesc();
            return setItemForm(results);
        //カテゴリが指定されている場合
        } else {
            try {
                Integer categoryId = Integer.parseInt(selectCategory);
                results = itemRepository.findByCategoryIdOrderByCreatedDateDesc(categoryId);
            } catch (NumberFormatException e) {
                results = itemRepository.findAllByOrderByCreatedDateDesc();
            }
            return setItemForm(results);
        }
    }

    private List<ItemForm> setItemForm(List<Item> results) {
        List<ItemForm> items = new ArrayList<>();

        for (Item result : results) {
            ItemForm itemForm = new ItemForm();
            itemForm.setId(String.valueOf(result.getId()));
            itemForm.setName(result.getName());
            itemForm.setPrice(String.valueOf(result.getPrice()));
            itemForm.setImage(result.getImage());
            itemForm.setStock(String.valueOf(result.getStock()));
            itemForm.setCategory_id(String.valueOf(result.getCategoryId()));
            items.add(itemForm);
        }
        return items;
    }
    public ItemForm findById (int id){
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            ItemForm itemForm = new ItemForm();
            itemForm.setId(String.valueOf(item.getId()));
            itemForm.setName(item.getName());
            itemForm.setPrice(String.valueOf(item.getPrice()));
            itemForm.setStock(String.valueOf(item.getStock()));
            itemForm.setImage(item.getImage());
            itemForm.setCategory_id(String.valueOf(item.getCategoryId()));
            return itemForm;
        } else {
            return null;
        }

    }
}
