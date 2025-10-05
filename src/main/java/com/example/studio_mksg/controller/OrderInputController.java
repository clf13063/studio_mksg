package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.CartItem;
import com.example.studio_mksg.controller.form.OrderForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.validator.OrderFormValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderInputController {
    @Autowired
    HttpSession session;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private OrderFormValidator orderFormValidator;

    @InitBinder("orderForm")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(orderFormValidator);
    }
    //購入情報入力画面表示
    @GetMapping("/orderInput")
        public ModelAndView showOrderInputForm(HttpServletRequest req){
        ModelAndView mav = new ModelAndView();

        // セッションからカート情報を取得
        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            // カートが空の場合はエラー表示してカート画面へ戻す
            mav.setViewName("redirect:/cart");
            mav.addObject("errorMessage", "カートが空です。");
            return mav;
        }

        // カート情報から注文フォームを作成
        OrderForm orderForm = new OrderForm();
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<String> itemNames = new ArrayList<>();
        List<String> itemImages = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        List<BigDecimal> subtotals = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart) {
            itemIds.add(ci.getId());
            quantities.add(ci.getQuantity());
            itemNames.add(ci.getName());
            itemImages.add(ci.getImage());
            BigDecimal price = BigDecimal.valueOf(ci.getPrice());
            BigDecimal sub = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
            prices.add(price);
            subtotals.add(sub);
            total = total.add(sub);
        }

        orderForm.setItemIds(itemIds);
        orderForm.setQuantities(quantities);
        orderForm.setItemNames(itemNames);
        orderForm.setItemImages(itemImages);
        orderForm.setPrices(prices);
        orderForm.setSubtotals(subtotals);
        orderForm.setTotalAmount(total);

        // セッションに保存
        session.setAttribute("orderForm", orderForm);

        // 画面に渡す
        mav.addObject("orderForm", orderForm);
        mav.addObject("cart", cart);
        mav.addObject("total", total);
        return mav;
    }

    @PostMapping("/orderInputCheck")
        public ModelAndView showOrderInputCheck(@Validated @ModelAttribute("orderForm") OrderForm orderForm, BindingResult
            result, RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderForm", result);
            redirectAttributes.addFlashAttribute("orderForm", orderForm);
            // 購入情報入力画面へリダイレクト
            return new ModelAndView("redirect:/orderInput");
        }
        session.setAttribute("orderForm", orderForm);
        return new ModelAndView("redirect:/orderConfirmation");
        }
    }
