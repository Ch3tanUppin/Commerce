package com.e.Commerce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e.Commerce.Model.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long>{
    
}
