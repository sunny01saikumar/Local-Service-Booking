package com.sai.geoLocation.order;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.OrderEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends CrudController<OrderEntity> {
    public OrderController(OrderEntityService service) {
        super(service);
    }
}
