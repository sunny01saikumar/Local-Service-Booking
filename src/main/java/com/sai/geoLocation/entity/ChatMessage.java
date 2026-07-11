package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {
    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(name = "message_type", nullable = false)
    private String messageType;

    private String content;

    @Column(name = "media_url")
    private String mediaUrl;

    private BigDecimal latitude;
    private BigDecimal longitude;

    @Column(name = "read_at")
    private OffsetDateTime readAt;
}
