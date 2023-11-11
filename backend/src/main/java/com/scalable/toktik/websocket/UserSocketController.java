package com.scalable.toktik.websocket;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.notification.NotificationRecordTool;
import com.scalable.toktik.service.NotificationService;
import com.scalable.toktik.websocket.type.NotificationType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class UserSocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRecordTool notificationRecordTool;
    private final NotificationService notificationService;

    public UserSocketController(SimpMessagingTemplate simpMessagingTemplate, NotificationRecordTool notificationRecordTool, NotificationService notificationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationRecordTool = notificationRecordTool;
        this.notificationService = notificationService;
    }

    public void sendNotification(UserModel user, VideoModel video, NotificationType type) {
        NotificationModel notification;
        switch (type) {
            case COMMENT -> {
                String content = "@" + user.getUsername() + " commented on your video";
                notification = notificationService.createNotification(video.getUser(), video, content);
            }
            case LIKE -> {
                String content = "@" + user.getUsername() + " liked on your video";
                notification = notificationService.createNotification(video.getUser(), video, content);
            }
            case DISLIKE -> {
                String content = "@" + user.getUsername() + " disliked on your video";
                notification = notificationService.createNotification(video.getUser(), video, content);
            }
            default -> {
                return;
            }
        }
        String path = "/sub/notification/" + video.getUser().getUsername();
        simpMessagingTemplate.convertAndSend(path, notificationRecordTool.createNotification(notification));
    }
}
