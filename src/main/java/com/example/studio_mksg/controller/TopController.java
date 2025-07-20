package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.ItemForm;
import com.example.studio_mksg.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TopController {
    @Autowired
    ItemService itemService;


    @GetMapping("/top")
    public ModelAndView top (@RequestParam(name = "selectCategory", required = false) String selectCategory) {
        ModelAndView mav = new ModelAndView();
        //商品全件取得
        List<ItemForm> AllItem = itemService.findAllItem(selectCategory);
        //ページ情報取得
        //Map<String, Object> findAllData = item.findAllItem(selectCategory);

        mav.setViewName("/top");
        mav.addObject("allItem",AllItem);
        mav.addObject("selectCategory",selectCategory);
        return mav;

    }
}