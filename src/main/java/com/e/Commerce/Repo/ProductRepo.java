package com.e.Commerce.Repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e.Commerce.Model.Category;
import com.e.Commerce.Model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);


    Page<Product> findByProductNameLikeIgnoreCase(String string, Pageable pageDetails);

    
}
