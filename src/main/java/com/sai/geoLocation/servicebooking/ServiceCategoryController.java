package com.sai.geoLocation.servicebooking;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.ServiceCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/service-categories")
public class ServiceCategoryController extends CrudController<ServiceCategory> {
    public ServiceCategoryController(ServiceCategoryService service) {
        super(service);
    }
}
