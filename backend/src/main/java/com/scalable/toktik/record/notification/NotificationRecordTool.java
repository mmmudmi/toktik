package com.scalable.toktik.record.notification;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.record.video.VideoRecordTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationRecordTool {
    private final VideoRecordTool videoRecordTool;

    public NotificationRecordTool(VideoRecordTool videoRecordTool) {
        this.videoRecordTool = videoRecordTool;
    }

    public NotificationRecord createNotification(NotificationModel notification) {
        return new NotificationRecord(videoRecordTool.createVideoSlimRecord(notification.getVideo()), notification.getContent(), notification.isRead(), notification.getCreated());
    }

    public List<NotificationRecord> createNotificationList(List<NotificationModel> notifications) {
        return notifications.stream().map(this::createNotification).toList();
    }
}
