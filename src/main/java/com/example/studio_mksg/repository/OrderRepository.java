package com.example.studio_mksg.repository;

import com.example.studio_mksg.repository.entity.Order;
import com.example.studio_mksg.repository.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

