package com.sai.geoLocation.notification;

import com.sai.geoLocation.entity.Notification;
import com.sai.geoLocation.repository.NotificationRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService extends CrudService<Notification> {
    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository repository) {
        super(repository);
        this.notifications = repository;
    }

    @Transactional
    public Notification sendNotification(UUID userId, String channel, String title, String body) {
        Notification notif = new Notification();
        notif.setUserId(userId);
        notif.setChannel(channel.toUpperCase());
        notif.setTitle(title);
        notif.setBody(body);
        notif.setStatus("SENT");
        
        System.out.println("==================================================");
        System.out.println("SIMULATED PUSH NOTIFICATION [" + channel.toUpperCase() + "] to User: " + userId);
        System.out.println("Title: " + title);
        System.out.println("Body: " + body);
        System.out.println("==================================================");
        return notifications.save(notif);
    }

    public List<Notification> getNotificationsForUser(UUID userId) {
        return notifications.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
