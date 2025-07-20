package com.example.studio_mksg.repository;

import com.example.studio_mksg.repository.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
        List<Item> findByCategoryId(int categoryId);
    }
