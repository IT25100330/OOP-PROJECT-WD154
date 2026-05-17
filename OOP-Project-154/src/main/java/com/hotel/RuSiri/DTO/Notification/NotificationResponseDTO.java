package com.hotel.RuSiri.DTO.Notification;
import com.hotel.RuSiri.Entity.Notification.NotificationType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDTO {

    private Long id;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt;
}
