package com.sai.geoLocation.product;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends CrudController<Product> {
    public ProductController(ProductService service) {
        super(service);
    }
}
