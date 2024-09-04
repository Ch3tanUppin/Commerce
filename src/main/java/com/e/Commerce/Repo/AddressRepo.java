package com.e.Commerce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e.Commerce.Model.Address;

public interface AddressRepo extends JpaRepository<Address, Long> {
    
}
