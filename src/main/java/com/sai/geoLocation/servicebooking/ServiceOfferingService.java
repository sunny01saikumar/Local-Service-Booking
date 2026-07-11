package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.entity.ServiceOffering;
import com.sai.geoLocation.repository.ServiceOfferingRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfferingService extends CrudService<ServiceOffering> {
    public ServiceOfferingService(ServiceOfferingRepository repository) {
        super(repository);
    }
}
