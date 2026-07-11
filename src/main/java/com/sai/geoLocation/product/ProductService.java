package com.sai.geoLocation.product;

import com.sai.geoLocation.entity.Product;
import com.sai.geoLocation.repository.ProductRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends CrudService<Product> {
    public ProductService(ProductRepository repository) {
        super(repository);
    }
}
