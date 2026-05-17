package com.hotel.RuSiri.Controller.Notification;

import com.hotel.RuSiri.DTO.Notification.NotificationResponseDTO;
import com.hotel.RuSiri.Service.Notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //  LIST ALL NOTIFICATION RELATED TO USER
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }


    // UNREAD COUNT    //TO DISPLAY IN NOTIFICATION BAR IN FRONTEND
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(notificationService.getUnreadCount());
    }

    // mark as read   //WHEN CLICK NOTIFICATION IN FRONTEND
    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }

    //WE create delete api when user want to delete that notification from notification bar
    // DELETE NOTIFICATION
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long id
    ) {

        notificationService.deleteNotification(id);

        return ResponseEntity.ok("Notification deleted successfully");
    }


}
