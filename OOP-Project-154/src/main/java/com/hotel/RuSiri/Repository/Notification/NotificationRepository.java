package com.hotel.RuSiri.Repository.Notification;

import com.hotel.RuSiri.Entity.Notification.Notification;

import com.hotel.RuSiri.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);

}