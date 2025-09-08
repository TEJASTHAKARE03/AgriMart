package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItems;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemsRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemsRepository cartItemsRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository, CartItemsRepository cartItemsRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    /**
     * Adds a specified quantity of a product to a user's shopping cart.
     *
     * @param userId the ID of the user
     * @param productId the ID of the product to add
     * @param quantity the number of products to add
     * @return the user's updated cart
     */
    @Transactional
    public Cart addProductToCart(Integer userId, Integer productId, int quantity) {
        // 1. Find the user and the product.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found."));

        // 2. Get the user's cart. If they don't have one, the User entity should create it.
        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalStateException("User does not have a cart.");
        }

        // 3. BUSINESS RULE: Check if the product is already in the cart.
        CartItems cartItem = cartItemsRepository.findByCartAndProduct(cart, product)
                .map(item -> {
                    // If it exists, update the quantity.
                    item.setQuantity(item.getQuantity() + quantity);
                    return item;
                })
                .orElseGet(() -> {
                    // If it doesn't exist, create a new cart item.
                    CartItems newItem = new CartItems();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(quantity);
                    cart.getCartItems().add(newItem); // Add to the cart's list of items
                    return newItem;
                });

        // 4. DATA ACCESS: Save the changes.
        cartItemsRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    /**
     * Removes a product entirely from the user's cart.
     *
     * @param userId the ID of the user
     * @param productId the ID of the product to remove
     */
    @Transactional
    public void removeProductFromCart(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found."));
        Cart cart = user.getCart();

        cartItemsRepository.findByCartAndProduct(cart, product).ifPresent(item -> {
            // Because of orphanRemoval=true on the Cart's cartItems list,
            // removing the item from the list and saving the cart will delete the item.
            cart.getCartItems().remove(item);
            cartItemsRepository.delete(item);
        });

        cartRepository.save(cart);
    }

    /**
     * Retrieves the user's cart.
     */
    public Cart getCartByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        return user.getCart();
    }
}
