package com.e.Commerce.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e.Commerce.Exceptions.ApiExecption;
import com.e.Commerce.Exceptions.ResoucreNotFoundException;
import com.e.Commerce.Model.Address;
import com.e.Commerce.Model.Cart;
import com.e.Commerce.Model.CartItem;
import com.e.Commerce.Model.Order;
import com.e.Commerce.Model.OrderItem;
import com.e.Commerce.Model.Payment;
import com.e.Commerce.Model.Product;
import com.e.Commerce.Repo.AddressRepo;
import com.e.Commerce.Repo.CartRepo;
import com.e.Commerce.Repo.OrderItemRepo;
import com.e.Commerce.Repo.OrderRepo;
import com.e.Commerce.Repo.PaymentRepo;
import com.e.Commerce.Repo.ProductRepo;
import com.e.Commerce.payload.OrderDTO;
import com.e.Commerce.payload.OrderItemDTO;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartService cartService;
    
    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepo.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResoucreNotFoundException("Cart", "email", emailId);
        }

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResoucreNotFoundException("Address", "addressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepo.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepo.save(order);

        List<CartItem> cartItems = cart.getCartItem();
        if (cartItems.isEmpty()) {
            throw new ApiExecption("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepo.saveAll(orderItems);

        cart.getCartItem().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            // Reduce stock quantity
            product.setQuantity(product.getQuantity() - quantity);

            // Save product back to the database
            productRepo.save(product);

            // Remove items from cart
            cartService.deleteProductFromCart(cart.getCartid(), item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }
}

