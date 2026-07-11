package com.sai.geoLocation.product;

import com.sai.geoLocation.entity.ProductVariant;
import com.sai.geoLocation.repository.ProductVariantRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantService extends CrudService<ProductVariant> {
    public ProductVariantService(ProductVariantRepository repository) {
        super(repository);
    }
}
