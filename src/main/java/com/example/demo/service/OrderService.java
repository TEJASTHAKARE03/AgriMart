package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.enums.OrderStatus;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    /**
     * Creates an order from the user's current cart.
     *
     * @param userId The ID of the user placing the order.
     * @return The newly created Order.
     * @throws IllegalStateException if the user, cart, or products are not found, or if the cart is empty.
     */
    @Transactional
    public Order createOrderFromCart(Integer userId) {
        // 1. Find the user and their cart.
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create order.");
        }

        // 2. Create the new Order object.
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Iterate through cart items and create order items.
        for (CartItems cartItem : new ArrayList<>(cart.getCartItems())) {
            Product product = cartItem.getProduct();

            // 4. BUSINESS RULE: Check for sufficient stock.
            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            // 5. Create a new OrderItems entity.
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // Lock the price at the time of order
            order.getOrderItems().add(orderItem);

            // 6. Update the total amount.
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            // 7. BUSINESS LOGIC: Decrease product stock.
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);

        // 8. DATA ACCESS: Save the new order. Cascade will save the order items.
        Order savedOrder = orderRepository.save(order);

        // 9. BUSINESS LOGIC: Clear the user's cart.
        // We iterate over a copy and clear the original list to avoid concurrent modification issues.
        for (CartItems item : new ArrayList<>(cart.getCartItems())) {
            cart.getCartItems().remove(item);
            cartItemsRepository.delete(item);
        }
        cartRepository.save(cart);

        return savedOrder;
    }

    /**
     * Finds all orders for a specific user, sorted by creation date.
     */
    public List<Order> findOrdersForUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
