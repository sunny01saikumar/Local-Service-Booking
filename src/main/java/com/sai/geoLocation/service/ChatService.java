package com.sai.geoLocation.service;

import com.sai.geoLocation.entity.ChatMessage;
import com.sai.geoLocation.entity.Booking;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import com.sai.geoLocation.repository.ChatMessageRepository;
import com.sai.geoLocation.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final ChatMessageRepository messages;
    private final BookingRepository bookings;

    public ChatService(ChatMessageRepository messages, BookingRepository bookings) {
        this.messages = messages;
        this.bookings = bookings;
    }

    @Transactional
    public ChatMessage sendMessage(UUID bookingId, UUID senderId, String messageType, String content,
                                  String mediaUrl, BigDecimal latitude, BigDecimal longitude) {
        Booking booking = bookings.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking room not found: " + bookingId));

        ChatMessage msg = new ChatMessage();
        msg.setBookingId(bookingId);
        msg.setSenderId(senderId);
        msg.setMessageType(messageType.toUpperCase());
        msg.setContent(content);
        msg.setMediaUrl(mediaUrl);
        msg.setLatitude(latitude);
        msg.setLongitude(longitude);
        return messages.save(msg);
    }

    public List<ChatMessage> getHistory(UUID bookingId) {
        return messages.findByBookingIdOrderByCreatedAtAsc(bookingId);
    }

    @Transactional
    public void markAsRead(UUID bookingId, UUID readerId) {
        List<ChatMessage> roomMessages = messages.findByBookingIdOrderByCreatedAtAsc(bookingId);
        boolean changed = false;
        for (ChatMessage msg : roomMessages) {
            if (!msg.getSenderId().equals(readerId) && msg.getReadAt() == null) {
                msg.setReadAt(OffsetDateTime.now());
                messages.save(msg);
                changed = true;
            }
        }
        if (changed) {
            System.out.println("WHATSAPP INTEGRATION: Mark chat read status dispatched for booking: " + bookingId);
        }
    }
}
