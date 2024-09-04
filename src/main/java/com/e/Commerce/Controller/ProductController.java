package com.e.Commerce.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.e.Commerce.Config.AppConstants;
import com.e.Commerce.Service.ProductService;
import com.e.Commerce.payload.ProductDTO;
import com.e.Commerce.payload.ProductResponse;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProduct( 
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,  
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,  
    @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,  
    @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
    @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
    @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,  
    @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponse productResponse = productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updateProductDTO = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
