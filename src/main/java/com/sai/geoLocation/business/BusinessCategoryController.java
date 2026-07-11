package com.sai.geoLocation.business;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.BusinessCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/business-categories")
public class BusinessCategoryController extends CrudController<BusinessCategory> {
    public BusinessCategoryController(BusinessCategoryService service) {
        super(service);
    }
}
