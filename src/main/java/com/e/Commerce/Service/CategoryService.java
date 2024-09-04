package com.e.Commerce.Service;

import com.e.Commerce.payload.CategoryDTO;
import com.e.Commerce.payload.CategoryResponse;

import jakarta.validation.Valid;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortby, String sortOrder);

    CategoryDTO categoryDTO(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);

    CategoryDTO createCategory(@Valid CategoryDTO categoryDTO);





}
