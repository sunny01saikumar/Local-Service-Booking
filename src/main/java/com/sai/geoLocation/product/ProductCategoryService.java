package com.sai.geoLocation.product;

import com.sai.geoLocation.entity.ProductCategory;
import com.sai.geoLocation.repository.ProductCategoryRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService extends CrudService<ProductCategory> {
    public ProductCategoryService(ProductCategoryRepository repository) {
        super(repository);
    }
}
