package com.example.studio_mksg.repository;

import com.example.studio_mksg.repository.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    //購入者側
    List<Item> findAllByOrderByCreatedDateDesc();
    List<Item> findByCategoryIdOrderByCreatedDateDesc(Integer categoryId);
    //販売者側
    List<Item> findAllByOrderById();
    List<Item> findByCategoryIdOrderById(@Param("categoryId") int categoryId);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.category WHERE i.category.id = :catId ORDER BY i.id ASC")
    List<Item> findByCategoryId(@org.springframework.data.repository.query.Param("catId") int categoryId);
    }
