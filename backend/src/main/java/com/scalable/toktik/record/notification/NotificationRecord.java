package com.scalable.toktik.record.notification;

import com.scalable.toktik.record.video.VideoSlimRecord;

import java.time.LocalDateTime;

public record NotificationRecord(VideoSlimRecord video, String notification, Boolean is_read, LocalDateTime created) {

}
