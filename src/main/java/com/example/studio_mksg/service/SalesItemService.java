package com.example.studio_mksg.service;

import com.example.studio_mksg.controller.form.ItemRegisterForm;
import com.example.studio_mksg.controller.form.ItemUpdateForm;
import com.example.studio_mksg.controller.form.SalesItemForm;
import com.example.studio_mksg.repository.CategoryRepository;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Category;
import com.example.studio_mksg.repository.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesItemService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageStorageService imageStorageService;
    
    public List<SalesItemForm> findAllSalesItem(String selectCategory) {

        List<Item> results;
        //カテゴリ指定されていない場合は全件取得
        if (selectCategory == null || selectCategory.isEmpty()) {
            results = itemRepository.findAllByOrderById();
            return setSalesItemForm(results);
            //カテゴリが指定されている場合
        } else {
            try {
                Integer categoryId = Integer.parseInt(selectCategory);
                results = itemRepository.findByCategoryIdOrderById(categoryId);
            } catch (NumberFormatException e) {
                results = itemRepository.findAllByOrderById();
            }
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
            salesItemForm.setVersion(result.getVersion());
            items.add(salesItemForm);
        }
        return items;
    }

    //登録
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

    //更新
    @Transactional
    public void update(ItemUpdateForm form) throws IOException {

        Item item = itemRepository.findByIdAndVersion(
                Integer.valueOf(form.getId()),
                form.getVersion()
        ).orElseThrow(() ->
                new ObjectOptimisticLockingFailureException(
                        Item.class,
                        form.getId()
                )
        );

        item.setName(form.getName());
        item.setPrice(BigDecimal.valueOf(Integer.parseInt(form.getPrice())));
        item.setStock(Integer.parseInt(form.getStock()));
        item.setCategoryId(form.getCategoryId());

        // 画像
        if (form.getImageFile() != null && !form.getImageFile().isEmpty()) {
            String newFileName = imageStorageService.saveImage(form.getImageFile());
            // 古い画像削除
            imageStorageService.deleteImage(item.getImage());
            item.setImage(newFileName);
        }

        itemRepository.save(item);
    }
}
