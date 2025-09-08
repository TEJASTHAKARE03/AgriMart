package com.example.demo.service;

import com.example.demo.dto.AddItemToCartRequestDTO;
import com.example.demo.dto.CartDetailDTO;
import com.example.demo.dto.CartItemDTO;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public CartDetailDTO addProductToCart(Integer userId, AddItemToCartRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found."));
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new IllegalStateException("Product not found."));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalStateException("User does not have a cart.");
        }

        CartItems cartItem = cartItemsRepository.findByCartAndProduct(cart, product)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + requestDTO.getQuantity());
                    return item;
                })
                .orElseGet(() -> {
                    CartItems newItem = new CartItems();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(requestDTO.getQuantity());
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItemsRepository.save(cartItem);
        Cart savedCart = cartRepository.save(cart);
        return convertToDetailDTO(savedCart);
    }

    @Transactional
    public CartDetailDTO removeProductFromCart(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException("Product not found."));
        Cart cart = user.getCart();

        cartItemsRepository.findByCartAndProduct(cart, product).ifPresent(item -> {
            cart.getCartItems().remove(item);
            cartItemsRepository.delete(item);
        });

        Cart savedCart = cartRepository.save(cart);
        return convertToDetailDTO(savedCart);
    }

    public CartDetailDTO getCartByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found."));
        return convertToDetailDTO(user.getCart());
    }

    // --- Helper Methods for DTO Conversion ---

    private CartDetailDTO convertToDetailDTO(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDetailDTO dto = new CartDetailDTO();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());

        List<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                .map(this::convertToItemDTO)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        BigDecimal totalAmount = itemDTOs.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalAmount(totalAmount);

        return dto;
    }

    private CartItemDTO convertToItemDTO(CartItems cartItem) {
        CartItemDTO dto = new CartItemDTO();
        Product product = cartItem.getProduct();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
