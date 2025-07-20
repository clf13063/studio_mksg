package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.OrderForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView showOrderConfirmationForm(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/orderConfirmation");
        OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");

        if (orderForm.getItemNames() == null || orderForm.getItemNames().isEmpty()) {
            List<String> itemNames = new ArrayList<>();
            List<BigDecimal> prices = new ArrayList<>();
            List<BigDecimal> subtotals = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i < orderForm.getItemIds().size(); i++) {
                int itemId = orderForm.getItemIds().get(i);
                int quantity = orderForm.getQuantities().get(i);
                Optional<Item> itemOpt = itemRepository.findById(itemId);

                if (itemOpt.isPresent()) {
                    Item item = itemOpt.get();
                    BigDecimal price = item.getPrice();
                    BigDecimal subTotal = price.multiply(BigDecimal.valueOf(quantity));

                    itemNames.add(item.getName());
                    prices.add(price);
                    subtotals.add(subTotal);
                    total = total.add(subTotal);
                }
            }

            orderForm.setItemNames(itemNames);
            orderForm.setPrices(prices);
            orderForm.setSubtotals(subtotals);
            orderForm.setTotalAmount(total);

            // 更新された情報で再保存
            session.setAttribute("orderForm", orderForm);
        }
        mav.addObject("orderForm", orderForm);
        return mav;

    }

    @PostMapping("/orderRegister")
    public ModelAndView orderRegister(){
        OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");
    // 注文情報をテーブルに格納
        orderService.saveOrder(orderForm);
    // 注文完了画面へリダイレクト
        return new ModelAndView("redirect:/orderComplete");
    }
}
