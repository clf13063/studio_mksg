package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.ItemForm;
import com.example.studio_mksg.controller.form.SalesItemForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.service.ItemService;
import com.example.studio_mksg.service.SalesItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SalesManagementTopController {
    @Autowired
    SalesItemService SalesItemService;
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/salesManagementTop")
    public ModelAndView salesManagementTop (@RequestParam(name = "selectCategory", required = false) String selectCategory) {
        ModelAndView mav = new ModelAndView();
        //商品全件取得
        List<SalesItemForm> salesItems = SalesItemService.findAllSalesItem(selectCategory);
//        List<SalesItemForm> salesItems = SalesItemService.findAllWithCategory();
        //ページ情報取得
        //Map<String, Object> findAllData = item.findAllItem(selectCategory);

        mav.setViewName("/salesManagementTop");
        mav.addObject("salesItems",salesItems);
        mav.addObject("selectCategory",selectCategory);
        return mav;
    }
}
