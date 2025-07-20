package com.example.studio_mksg.controller;

import com.example.studio_mksg.controller.form.OrderForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.validator.OrderFormValidator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
        public ModelAndView showOrderInputForm(){
        ModelAndView mav = new ModelAndView();
        OrderForm orderForm = (OrderForm) session.getAttribute("orderForm");

        if (orderForm == null) {
            orderForm = new OrderForm();
            // ★ 仮の商品情報をセッションに追加する（例：itemId=1を2個、itemId=3を1個）
            // ※画面からの選択ができるようになるまでは仮データを使う
            // 仮の商品情報（itemId=1を2個、itemId=3を1個）
            List<Integer> itemIds = List.of(1, 3);
            List<Integer> quantities = List.of(2, 1);

            List<String> itemNames = new ArrayList<>();
            List<BigDecimal> prices = new ArrayList<>();
            List<BigDecimal> subtotals = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i < itemIds.size(); i++) {
                int itemId = itemIds.get(i);
                int quantity = quantities.get(i);
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

            // OrderForm にセット
            orderForm.setItemIds(itemIds);
            orderForm.setQuantities(quantities);
            orderForm.setItemNames(itemNames);
            orderForm.setPrices(prices);
            orderForm.setSubtotals(subtotals);
            orderForm.setTotalAmount(total);

            session.setAttribute("orderForm", orderForm);
        }

        mav.addObject("orderForm", orderForm);
        mav.setViewName("orderInput");
        return mav;
    }

    @PostMapping("/orderInputCheck")
        public ModelAndView showOrderInputCheck(@Validated @ModelAttribute("orderForm") OrderForm orderForm, BindingResult
            result, RedirectAttributes redirectAttributes){

        // ★ 仮の商品情報をセッションに追加する（例：itemId=1を2個、itemId=3を1個）
        // ※画面からの選択ができるようになるまでは仮データを使う
        // 仮の商品情報（itemId=1を2個、itemId=3を1個）
        List<Integer> itemIds = List.of(1, 3);
        List<Integer> quantities = List.of(2, 1);

        List<String> itemNames = new ArrayList<>();
        List<BigDecimal> prices = new ArrayList<>();
        List<BigDecimal> subtotals = new ArrayList<>();


        // 合計金額の計算も仮で入れておく（商品が存在すれば）
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < itemIds.size(); i++) {
            int itemId = itemIds.get(i);
            int quantity = quantities.get(i);
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
        // OrderForm にセット
        orderForm.setItemIds(itemIds);
        orderForm.setQuantities(quantities);
        orderForm.setItemNames(itemNames);
        orderForm.setPrices(prices);
        orderForm.setSubtotals(subtotals);
        orderForm.setTotalAmount(total);
        session.setAttribute("orderForm", orderForm);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderForm", result);
            redirectAttributes.addFlashAttribute("orderForm", orderForm);
            // 購入情報入力画面へリダイレクト
            return new ModelAndView("redirect:/orderInput");
        }
        return new ModelAndView("redirect:/orderConfirmation");
        }
    }
