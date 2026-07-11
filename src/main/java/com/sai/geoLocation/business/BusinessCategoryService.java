package com.sai.geoLocation.business;

import com.sai.geoLocation.entity.BusinessCategory;
import com.sai.geoLocation.repository.BusinessCategoryRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class BusinessCategoryService extends CrudService<BusinessCategory> {
    public BusinessCategoryService(BusinessCategoryRepository repository) {
        super(repository);
    }
}
