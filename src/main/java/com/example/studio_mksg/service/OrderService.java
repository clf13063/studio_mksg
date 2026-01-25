package com.example.studio_mksg.service;

import com.example.studio_mksg.controller.form.CartItem;
import com.example.studio_mksg.controller.form.OrderForm;
import com.example.studio_mksg.repository.ItemRepository;
import com.example.studio_mksg.repository.OrderDetailRepository;
import com.example.studio_mksg.repository.OrderRepository;
import com.example.studio_mksg.repository.entity.Item;
import com.example.studio_mksg.repository.entity.Order;
import com.example.studio_mksg.repository.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Transactional
    public Order saveOrder(OrderForm orderForm, List<CartItem> cart) {

        if (cart == null || cart.isEmpty()) {
            throw new IllegalStateException("カートが空です");
        }

        boolean shortage = false;
        // 1. 在庫チェック（cartから確認）
        for (CartItem ci : cart) {

            Item item = itemRepository.findById(ci.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("商品が見つかりません: ID=" + ci.getId()));

            int stock = item.getStock();
            int qty = ci.getQuantity();

            if (qty > stock) {
                ci.setQuantity(stock);
                shortage = true;
            }
        }

        if (shortage) {
            throw new IllegalStateException(
                    "在庫不足の商品があったため、数量を調整しました。"
            );
        }

        // 2. 注文情報作成（顧客情報のみ）
        Order order = new Order();
        order.setName(orderForm.getLastName() + " " + orderForm.getFirstName());
        order.setPostcode(orderForm.getPostcode());
        order.setAddress(orderForm.getAddress());
        order.setTel(orderForm.getTel());

        // お届け先
        if (Boolean.TRUE.equals(orderForm.getDifferentReceiver())) {
            order.setReceiverName(orderForm.getReceiverLastName() + " " + orderForm.getReceiverFirstName());
            order.setReceiverPostcode(orderForm.getReceiverPostcode());
            order.setReceiverAddress(orderForm.getReceiverAddress());
            order.setReceiverTel(orderForm.getReceiverTel());
        } else {
            order.setReceiverName(order.getName());
            order.setReceiverPostcode(order.getPostcode());
            order.setReceiverAddress(order.getAddress());
            order.setReceiverTel(order.getTel());
        }

        order.setEmail(orderForm.getEmail());
        order.setPaymentMethod(orderForm.getPaymentMethod());
        order.setGiftHope(orderForm.getGiftHope());
        order.setGiftMassage(Boolean.TRUE.equals(orderForm.getGiftMassageFlag()) ? orderForm.getGiftMassage() : null);

        // 3. 注文詳細作成（cart から作成）
        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart) {

            Item item = itemRepository.findById(ci.getId()).get();

            // 在庫減算
            item.setStock(item.getStock() - ci.getQuantity());
            itemRepository.save(item);

            // 小計
            BigDecimal subTotal = item.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));

            // 注文詳細
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setItem(item);
            detail.setQuantity(ci.getQuantity());
            detail.setSubTotal(subTotal);

            orderDetails.add(detail);
            total = total.add(subTotal);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}
