package com.e.Commerce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e.Commerce.Model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    
}
