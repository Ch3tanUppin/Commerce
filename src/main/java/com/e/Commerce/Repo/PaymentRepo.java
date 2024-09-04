package com.e.Commerce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e.Commerce.Model.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{
    
}
