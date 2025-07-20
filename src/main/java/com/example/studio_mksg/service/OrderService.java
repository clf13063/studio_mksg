package com.example.studio_mksg.service;

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
    public Order saveOrder(OrderForm orderForm) {
        // 1. 在庫チェック
        for (int i = 0; i < orderForm.getItemIds().size(); i++) {
            int itemId = orderForm.getItemIds().get(i);
            int quantity = orderForm.getQuantities().get(i);

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("商品が見つかりません: ID=" + itemId));
            if (item.getStock() < quantity) {
                throw new IllegalStateException("在庫不足: " + item.getName());
            }
        }

        // 2. 注文情報の作成
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

        // その他
        order.setEmail(orderForm.getEmail());
        order.setPaymentMethod(orderForm.getPaymentMethod());
        order.setGiftHope(orderForm.getGiftHope());
        order.setGiftMassage(Boolean.TRUE.equals(orderForm.getGiftMassageFlag()) ? orderForm.getGiftMassage() : null);


        // 3. 注文詳細の作成
        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < orderForm.getItemIds().size(); i++) {
            int itemId = orderForm.getItemIds().get(i);
            int quantity = orderForm.getQuantities().get(i);

            Item item = itemRepository.findById(itemId).get();

            // 在庫更新
            item.setStock(item.getStock() - quantity);
            itemRepository.save(item);

            // 小計
            BigDecimal subTotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));

            // 注文詳細作成
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setItem(item);
            detail.setQuantity(quantity);
            detail.setSubTotal(subTotal);

            orderDetails.add(detail);
            total = total.add(subTotal);
        }

        order.setOrderDetails(orderDetails);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }
}
