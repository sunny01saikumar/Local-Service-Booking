package com.sai.geoLocation.product;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.ProductVariant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController extends CrudController<ProductVariant> {
    public ProductVariantController(ProductVariantService service) {
        super(service);
    }
}
