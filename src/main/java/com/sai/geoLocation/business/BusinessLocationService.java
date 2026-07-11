package com.sai.geoLocation.business;

import com.sai.geoLocation.entity.BusinessLocation;
import com.sai.geoLocation.repository.BusinessLocationRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class BusinessLocationService extends CrudService<BusinessLocation> {
    public BusinessLocationService(BusinessLocationRepository repository) {
        super(repository);
    }
}
