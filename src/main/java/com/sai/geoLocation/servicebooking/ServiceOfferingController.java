package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.ServiceOffering;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceOfferingController extends CrudController<ServiceOffering> {
    public ServiceOfferingController(ServiceOfferingService service) {
        super(service);
    }
}
