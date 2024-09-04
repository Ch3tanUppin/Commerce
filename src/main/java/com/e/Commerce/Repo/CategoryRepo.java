package com.e.Commerce.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e.Commerce.Model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {


    Category findByCategoryName(String categoryName);
    
}
