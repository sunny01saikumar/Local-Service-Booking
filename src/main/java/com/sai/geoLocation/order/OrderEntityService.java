package com.sai.geoLocation.order;

import com.sai.geoLocation.entity.OrderEntity;
import com.sai.geoLocation.repository.OrderEntityRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class OrderEntityService extends CrudService<OrderEntity> {
    public OrderEntityService(OrderEntityRepository repository) {
        super(repository);
    }
}
