package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.ItemRegisterForm;
import com.example.studio_mksg.repository.CategoryRepository;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.service.ItemService;
import com.example.studio_mksg.service.SalesItemService;
import com.example.studio_mksg.validator.ItemRegisterFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ItemRegisterController {
    @Autowired
    SalesItemService salesItemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ItemRegisterFormValidator itemRegisterFormValidator;

    @InitBinder("itemRegisterForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(itemRegisterFormValidator);
    }

    // 商品追加画面表示（GET）
    @GetMapping("/itemRegister")
    public String showRegisterForm(Model model) {
        model.addAttribute("itemRegisterForm", new ItemRegisterForm());
        model.addAttribute("categories", categoryRepository.findAll());
        return "itemRegister";
    }

    // 商品登録処理（POST）
    @PostMapping("/itemRegister")
    public ModelAndView registerItem(
            @Valid @ModelAttribute("itemRegisterForm") ItemRegisterForm itemRegisterForm,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            ModelAndView mav = new ModelAndView("itemRegister");
            return mav;
        }

        // 登録実行
        salesItemService.registerItem(itemRegisterForm);
        return new ModelAndView("redirect:/salesManagementTop");
    }
}
