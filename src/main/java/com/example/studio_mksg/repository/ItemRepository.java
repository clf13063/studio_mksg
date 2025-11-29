package com.example.studio_mksg.repository;

import com.example.studio_mksg.repository.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.category ORDER BY i.id ASC")
        List<Item> findByCategoryId(int categoryId);
    }
