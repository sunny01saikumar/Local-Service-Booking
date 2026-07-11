package com.sai.geoLocation.cart;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Cart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController extends CrudController<Cart> {
    public CartController(CartService service) {
        super(service);
    }
}
