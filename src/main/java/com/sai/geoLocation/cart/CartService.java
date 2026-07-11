package com.sai.geoLocation.cart;

import com.sai.geoLocation.entity.Cart;
import com.sai.geoLocation.repository.CartRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class CartService extends CrudService<Cart> {
    public CartService(CartRepository repository) {
        super(repository);
    }
}
