package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.entity.ServiceCategory;
import com.sai.geoLocation.repository.ServiceCategoryRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ServiceCategoryService extends CrudService<ServiceCategory> {
    public ServiceCategoryService(ServiceCategoryRepository repository) {
        super(repository);
    }
}
