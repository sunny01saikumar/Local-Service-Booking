package com.sai.geoLocation.cart;

import com.sai.geoLocation.entity.CartItem;
import com.sai.geoLocation.repository.CartItemRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class CartItemService extends CrudService<CartItem> {
    public CartItemService(CartItemRepository repository) {
        super(repository);
    }
}
