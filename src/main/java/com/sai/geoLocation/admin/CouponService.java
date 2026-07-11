package com.sai.geoLocation.admin;

import com.sai.geoLocation.entity.Coupon;
import com.sai.geoLocation.repository.CouponRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class CouponService extends CrudService<Coupon> {
    public CouponService(CouponRepository repository) {
        super(repository);
    }
}
