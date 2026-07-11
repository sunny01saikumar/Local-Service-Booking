package com.sai.geoLocation.product;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.ProductCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController extends CrudController<ProductCategory> {
    public ProductCategoryController(ProductCategoryService service) {
        super(service);
    }
}
