package com.example.studio_mksg.controller;

import ch.qos.logback.core.model.Model;
import com.example.studio_mksg.controller.form.CartItem;
import com.example.studio_mksg.controller.form.ItemForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.service.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TopController {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;


    @GetMapping("/top")
    public ModelAndView top (@RequestParam(name = "selectCategory", required = false) String selectCategory) {
        ModelAndView mav = new ModelAndView();
        //商品全件取得
        List<ItemForm> AllItem = itemService.findAllItem(selectCategory);
        //ページ情報取得
        //Map<String, Object> findAllData = item.findAllItem(selectCategory);

        //商品カートへ追加


        mav.setViewName("/top");
        mav.addObject("allItem",AllItem);
        mav.addObject("selectCategory",selectCategory);
        return mav;

    }
    @GetMapping("/items/{id}")
    public ModelAndView transition (@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
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

            mav.addObject("items", itemForm);
            mav.setViewName("itemdetail");
        } else {
            mav.setViewName("redirect:/top");
            mav.addObject("errorMessage","商品が見つかりませんでした。");
        }
        return mav;
    }

    @GetMapping("/cart/{id}")
    public ModelAndView addToCart(@PathVariable int id, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        HttpSession session = req.getSession();
        Optional<Item> itemOpt = itemRepository.findById(id);

        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();

            // セッションからカート取得
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            // すでにカートにあるか確認
            CartItem existing = null;
            for (CartItem ci : cart) {
                if (ci.getId() == item.getId()) {
                    existing = ci;
                    break;
                }
            }

            if (existing != null) {
                // 数量を1増やす
                existing.setQuantity(existing.getQuantity() + 1);
            } else {
                // 新しくカートに追加
                CartItem newItem = new CartItem();
                newItem.setId(item.getId());
                newItem.setName(item.getName());
                newItem.setPrice(item.getPrice());
                newItem.setImage(item.getImage());
                newItem.setQuantity(1);
                newItem.setImage(item.getImage());
                cart.add(newItem);
            }

            // 合計金額計算
            int total = 0;
            for (CartItem ci : cart) {
                total += ci.getPrice() * ci.getQuantity();
            }

            // セッション更新
            session.setAttribute("cart", cart);

            // 画面へ渡す
            mav.addObject("cart", cart);
            mav.addObject("total", total);
            mav.setViewName("shoppingCart");
        } else {
            mav.setViewName("redirect:/top");
            mav.addObject("errorMessage", "商品が見つかりませんでした。");
        }

        return mav;
    }
    @PostMapping("/cart/remove")
    public ModelAndView removeFromCart(@RequestParam int id,
                                       HttpServletRequest req) {
        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // id が一致する商品を削除
        cart.removeIf(ci -> ci.getId() == id);

        // 合計金額再計算
        int total = 0;
        for (CartItem ci : cart) {
            total += ci.getSubTotal();
        }
        session.setAttribute("cart", cart);

        ModelAndView mav = new ModelAndView("shoppingCart");
        mav.addObject("cart", cart);
        mav.addObject("total", total);

        return mav;
    }
    @PostMapping("/cart/update")
    public ModelAndView updateCart(@RequestParam int id,
                                   @RequestParam String action,
                                   HttpServletRequest req) {
        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        CartItem target = null;
        for (CartItem ci : cart) {
            if (ci.getId() == id) {
                target = ci;
                break;
            }
        }

        if (target != null) {
            if ("plus".equals(action)) {
                target.setQuantity(target.getQuantity() + 1);
            } else if ("minus".equals(action)) {
                target.setQuantity(target.getQuantity() - 1);
                if (target.getQuantity() <= 0) {
                    cart.remove(target);
                }
            }
        }

        // 合計再計算
        int total = 0;
        for (CartItem ci : cart) {
            total += ci.getPrice() * ci.getQuantity();
        }

        session.setAttribute("cart", cart);

        ModelAndView mav = new ModelAndView("shoppingCart");
        mav.addObject("cart", cart);
        mav.addObject("total", total);

        return mav;
    }
    //トップ画面からカート画面へ遷移する
    @GetMapping("/cart")
    public ModelAndView viewCart(HttpServletRequest req) {
        HttpSession session = req.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 合計金額計算
        int total = 0;
        for (CartItem ci : cart) {
            total += ci.getSubTotal();
        }

        ModelAndView mav = new ModelAndView("shoppingCart");
        mav.addObject("cart", cart);
        mav.addObject("total", total);

        return mav;
    }
}