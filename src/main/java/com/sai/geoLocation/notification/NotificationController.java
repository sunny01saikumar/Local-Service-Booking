package com.sai.geoLocation.notification;

import com.sai.geoLocation.controller.CrudController;
import com.sai.geoLocation.entity.Notification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController extends CrudController<Notification> {
    public NotificationController(NotificationService service) {
        super(service);
    }
}
