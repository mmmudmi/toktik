package com.scalable.toktik.redis.queue;

import com.scalable.toktik.record.notification.NotificationRecord;
import com.scalable.toktik.websocket.type.NotificationType;

public record NotificationQueue(NotificationType type, NotificationRecord record) {
}
