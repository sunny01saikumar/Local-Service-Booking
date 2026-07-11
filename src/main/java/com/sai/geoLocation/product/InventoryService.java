package com.sai.geoLocation.product;

import com.sai.geoLocation.entity.Inventory;
import com.sai.geoLocation.repository.InventoryRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends CrudService<Inventory> {
    public InventoryService(InventoryRepository repository) {
        super(repository);
    }
}
