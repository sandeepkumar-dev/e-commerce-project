package com.cart_service.controller;


import com.cart_service.dto.AddToCartRequest;
import com.cart_service.entity.Cart;
import com.cart_service.repository.CartRepository;
import com.cart_service.service.CartService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private  final CartService cartService;
    private final CartRepository cartRepository;

    public CartController(CartService cartService,
                          CartRepository cartRepository) {
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestHeader(value = "X-CART-ID", required = false) String uuid,
            @RequestBody AddToCartRequest request
            ){

        Cart cart = cartService.addToCart(uuid,request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CART-ID", cart.getUuid());

        return ResponseEntity.ok()
                .headers(headers)
                .body("product added to cart");

    }
    @GetMapping("/{uuid}")
    public  Cart getCard(@PathVariable String uuid){
        return cartRepository.findByUuid(uuid)
                .orElseThrow(()-> new RuntimeException("Cart not found"));
    }

    @DeleteMapping("/{uuid}/clear")
    public void  clearCart (@PathVariable String uuid){
        Cart cart = cartRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
