package com.scalable.toktik.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.notification.NotificationRecordTool;
import com.scalable.toktik.record.socket.SocketStandardRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.service.NotificationService;
import com.scalable.toktik.util.JsonConverter;
import com.scalable.toktik.websocket.type.NotificationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class UserSocketController {
    private final NotificationRecordTool notificationRecordTool;
    private final NotificationService notificationService;
    private final RedisService redisService;

    @Value("${redis.channel.socket}")
    private String socketChannel;

    public UserSocketController(NotificationRecordTool notificationRecordTool, NotificationService notificationService, RedisService redisService) {
        this.notificationRecordTool = notificationRecordTool;
        this.notificationService = notificationService;
        this.redisService = redisService;
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
        String endpoint = "/sub/notification/" + video.getUser().getUsername();
        SocketStandardRecord content = new SocketStandardRecord(endpoint, notificationRecordTool.createNotification(notification));
        try {
            redisService.publish(socketChannel, JsonConverter.encoding(content));
        } catch (JsonProcessingException ignored) {
        }
    }
}
