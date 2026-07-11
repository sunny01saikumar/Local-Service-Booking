package com.sai.geoLocation.payment;

import com.sai.geoLocation.entity.Payment;
import com.sai.geoLocation.repository.PaymentRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class PaymentService extends CrudService<Payment> {
    public PaymentService(PaymentRepository repository) {
        super(repository);
    }
}
