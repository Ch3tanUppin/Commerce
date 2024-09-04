package com.e.Commerce.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.e.Commerce.payload.ProductDTO;
import com.e.Commerce.payload.ProductResponse;

public interface ProductService {


    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize,String sortby, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(ProductDTO productDTO, Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductDTO deleteProduct(Long productId);

}
