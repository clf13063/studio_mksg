package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.CartItem;
import com.example.studio_mksg.controller.form.OrderForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderConfirmationController {
    @Autowired
    HttpSession session;
    @Autowired
    OrderService orderService;
    @Autowired
    ItemRepository itemRepository;

    //購入情報確認画面表示
    @GetMapping("/orderConfirmation")
    public ModelAndView showOrderConfirmation(HttpServletRequest req) {
        HttpSession session = req.getSession();

        // カート情報
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return new ModelAndView("redirect:/cart");
        }

        // 顧客情報
        OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
        if (orderForm == null) {
            orderForm = new OrderForm();
        }

        // 合計金額を計算
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart) {
            BigDecimal price = BigDecimal.valueOf(ci.getPrice());
            total = total.add(price.multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        ModelAndView mav = new ModelAndView("orderConfirmation");
        mav.addObject("cart", cart);
        mav.addObject("total", total);
        mav.addObject("orderForm", orderForm);

        return mav;
    }

    @PostMapping("/orderRegister")
    public ModelAndView orderRegister(HttpServletRequest req, RedirectAttributes redirectAttributes){
        HttpSession session = req.getSession();

        OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            // カートが空の場合はエラー表示してカート画面へ戻す
            ModelAndView mav = new ModelAndView("redirect:/cart");
            mav.addObject("errorMessage", "カートが空です。");
            return mav;
        }
    // 注文情報をテーブルに格納
        try {
            orderService.saveOrder(orderForm, cart);
        } catch (IllegalStateException e) {
            // 在庫不足だけをユーザー向けに表示
            if (e.getMessage().contains("在庫")) {
                // 在庫不足の場合はエラー表示してカート画面へ戻す
                ModelAndView mav = new ModelAndView("redirect:/cart");
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                return mav;
            }
            // それ以外は想定外エラー
            throw e;
        }
    // 注文完了後はカート、注文情報削除
        session.removeAttribute("cart");
        session.removeAttribute("orderForm");
    // 注文完了画面へリダイレクト
        return new ModelAndView("redirect:/orderComplete");
    }
}
