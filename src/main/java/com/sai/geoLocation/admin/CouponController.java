package com.sai.geoLocation.admin;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Coupon;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/coupons")
public class CouponController extends CrudController<Coupon> {
    public CouponController(CouponService service) {
        super(service);
    }
}
