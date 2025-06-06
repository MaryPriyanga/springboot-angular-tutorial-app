package com.snipe.learning.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendCourseApprovalNotification(Integer instructorId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications-" + instructorId, message);
    }
    
    public void sendCourseRejectNotification(Integer instructorId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications-" + instructorId, message);
    }
}

