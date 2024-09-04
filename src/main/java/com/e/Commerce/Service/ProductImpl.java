package com.e.Commerce.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.e.Commerce.Exceptions.ApiExecption;
import com.e.Commerce.Exceptions.ResoucreNotFoundException;
import com.e.Commerce.Model.Cart;
import com.e.Commerce.Model.Category;
import com.e.Commerce.Model.Product;
import com.e.Commerce.Repo.CartRepo;
import com.e.Commerce.Repo.CategoryRepo;
import com.e.Commerce.Repo.ProductRepo;
import com.e.Commerce.payload.CartDTO;
import com.e.Commerce.payload.ProductDTO;
import com.e.Commerce.payload.ProductResponse;

@Service
public class ProductImpl implements ProductService {

        @Autowired
        private ProductRepo productRepo;

        @Autowired
        private CategoryRepo categoryRepo;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private CartService cartService;

        @Autowired
        private CartRepo cartRepo;

        @Autowired
        private FileService fileService;

        @Value("${project.image}")
        private String path;

        @Override
        public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
                Category category = categoryRepo.findById(categoryId)
                                .orElseThrow(() -> new ResoucreNotFoundException("Category", "categoryId", categoryId));

                boolean isProductNotPresent = true;

                List<Product> products = category.getProducts();
                for (Product value : products) {
                        if (value.getProductName().equals(productDTO.getProductName())) {
                                isProductNotPresent = false;
                                break;
                        }

                }

                if (isProductNotPresent) {
                        Product product = modelMapper.map(productDTO, Product.class);
                        product.setImage("default.png");
                        product.setCategory(category);
                        double specialPrice = product.getPrice() -
                                        ((product.getDiscount() * 0.01) * product.getPrice());
                        product.setSpecialPrice(specialPrice);
                        Product savedProduct = productRepo.save(product);
                        return modelMapper.map(savedProduct, ProductDTO.class);
                } else {
                        throw new ApiExecption(" Product Already Exist");
                }
        }

        @Override
        public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
                Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
                Page<Product> productPage = productRepo.findAll(pageDetails);

                List<Product> products = productPage.getContent();
                List<ProductDTO> productDTOS = products.stream()
                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                .toList();

                ProductResponse productResponse = new ProductResponse();
                productResponse.setContent(productDTOS);
                productResponse.setPageNumber(productPage.getNumber());
                productResponse.setPageSize(productPage.getSize());
                productResponse.setTotalElements((int) productPage.getTotalElements());
                productResponse.setTotalPages(productPage.getTotalPages());
                productResponse.setLastPage(productPage.isLast());
                return productResponse;
        }

        @Override
        public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                        String sortOrder) {
                Category category = categoryRepo.findById(categoryId)
                                .orElseThrow(() -> new ResoucreNotFoundException("Category", "categoryId", categoryId));

                Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy)
                                .ascending() : Sort.by(sortBy).descending();

                Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
                Page<Product> productPage = productRepo.findByCategoryOrderByPriceAsc(category, pageDetails);

                List<Product> products = productPage.getContent();

                if (products.isEmpty()) {
                        throw new ApiExecption(category.getCategoryName() + " Category does not have any products");
                }

                List<ProductDTO> productDTOS = products.stream()
                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                .toList();

                ProductResponse productResponse = new ProductResponse();
                productResponse.setContent(productDTOS);
                productResponse.setPageNumber(productPage.getNumber());
                productResponse.setPageSize(productPage.getSize());
                productResponse.setTotalElements((int) productPage.getTotalElements());
                productResponse.setTotalPages(productPage.getTotalPages());
                productResponse.setLastPage(productPage.isLast());
                return productResponse;
        }

        @Override
        public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize,
                        String sortBy,
                        String sortOrder) {
                Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy)
                                .ascending() : Sort.by(sortBy).descending();

                Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
                Page<Product> productPage = productRepo.findByProductNameLikeIgnoreCase('%' + keyword + '%',
                                pageDetails);

                List<Product> products = productPage.getContent();
                List<ProductDTO> productDTOS = products.stream()
                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                .toList();
                if (products.isEmpty()) {
                        throw new ApiExecption("Products Not found with :" + keyword);
                }

                ProductResponse productResponse = new ProductResponse();
                productResponse.setContent(productDTOS);
                productResponse.setPageNumber(productPage.getNumber());
                productResponse.setPageSize(productPage.getSize());
                productResponse.setTotalElements((int) productPage.getTotalElements());
                productResponse.setTotalPages(productPage.getTotalPages());
                productResponse.setLastPage(productPage.isLast());
                return productResponse;
        }

        @Override
        public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
                Product productFromDb = productRepo.findById(productId)
                                .orElseThrow(() -> new ResoucreNotFoundException("Product", "productId", productId));

                Product product = modelMapper.map(productDTO, Product.class);

                productFromDb.setProductName(product.getProductName());
                productFromDb.setDescription(product.getDescription());
                productFromDb.setQuantity(product.getQuantity());
                productFromDb.setDiscount(product.getDiscount());
                productFromDb.setPrice(product.getPrice());
                productFromDb.setSpecialPrice(product.getSpecialPrice());

                Product savedProduct = productRepo.save(productFromDb);
                List<Cart> carts = cartRepo.findCartsByProductId(productId);

                List<CartDTO> cartDTOs = carts.stream().map(cart -> {
                        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                        List<ProductDTO> products = cart.getCartItem().stream()
                                        .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                                        .collect(Collectors.toList());

                        cartDTO.setProducts(products);

                        return cartDTO;

                }).collect(Collectors.toList());

                cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

                return modelMapper.map(savedProduct, ProductDTO.class);
        }

        @Override
        public ProductDTO deleteProduct(Long productId) {
                Product product = productRepo.findById(productId)
                                .orElseThrow(() -> new ResoucreNotFoundException("Product", "productId", productId));

                List<Cart> carts = cartRepo.findCartsByProductId(productId);
                carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartid(), productId));
                productRepo.delete(product);
                return modelMapper.map(product, ProductDTO.class);
        }

        @Override
        public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
                Product productFromDb = productRepo.findById(productId)
                                .orElseThrow(() -> new ResoucreNotFoundException("Product", "productId", productId));

                String fileName = fileService.uploadImage(path, image);
                productFromDb.setImage(fileName);

                Product updatedProduct = productRepo.save(productFromDb);
                return modelMapper.map(updatedProduct, ProductDTO.class);
        }

}
