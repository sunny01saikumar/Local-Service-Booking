package com.sai.geoLocation.payment;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Payment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController extends CrudController<Payment> {
    public PaymentController(PaymentService service) {
        super(service);
    }
}
