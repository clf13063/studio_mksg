package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.ItemUpdateForm;
import com.example.studio_mksg.controller.form.SalesItemForm;
import com.example.studio_mksg.repository.CategoryRepository;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.service.SalesItemService;
import com.example.studio_mksg.validator.ItemUpdateFormValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class SalesManagementTopController {
    @Autowired
    SalesItemService salesItemService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ItemUpdateFormValidator itemUpdateFormValidator;

    @InitBinder("itemUpdateForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(itemUpdateFormValidator);
    }

    @GetMapping("/salesManagementTop")
    public ModelAndView salesManagementTop (@RequestParam(name = "selectCategory", required = false) String selectCategory) {
        ModelAndView mav = new ModelAndView();
        //商品全件取得
        List<SalesItemForm> salesItems = salesItemService.findAllSalesItem(selectCategory);
//        List<SalesItemForm> salesItems = SalesItemService.findAllWithCategory();
        //ページ情報取得
        //Map<String, Object> findAllData = item.findAllItem(selectCategory);

        mav.setViewName("/salesManagementTop");
        mav.addObject("salesItems",salesItems);
        mav.addObject("selectCategory",selectCategory);
        mav.addObject("categories", categoryRepository.findAll());
        mav.addObject("itemUpdateForm", new ItemUpdateForm());
        return mav;
    }

    //更新
    @PostMapping("/itemUpdate")
    public String update(
            @Valid @ModelAttribute("itemUpdateForm") ItemUpdateForm itemUpdateForm,
            BindingResult result,
            Model model, RedirectAttributes redirectAttributes) throws IOException {

        if (result.hasErrors()) {
            // カテゴリ再セット
            model.addAttribute("categories", categoryRepository.findAll());
            // モーダル再表示フラグ
            model.addAttribute("isEditModalOpen", true);
            // 一覧再取得
            List<SalesItemForm> salesItems = salesItemService.findAllSalesItem(null);
            model.addAttribute("salesItems", salesItems);

            return "salesManagementTop";
        }

        //排他制御
        try {
            salesItemService.update(itemUpdateForm);
        }catch (ObjectOptimisticLockingFailureException e) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "他のユーザーに更新されました。再度開き直してください。"
                );
                return "redirect:/salesManagementTop";
            }

        redirectAttributes.addFlashAttribute("successMessage", "更新完了しました");

        return "redirect:/salesManagementTop";
    }

    //削除
    @PostMapping("/itemDelete/{id}")
    public String delete(
            @PathVariable String id,
            RedirectAttributes redirectAttributes) {

        salesItemService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "削除しました");

        return "redirect:/salesManagementTop";
    }
}
