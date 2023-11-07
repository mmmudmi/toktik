package com.scalable.toktik.service;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationModel createNotification(UserModel user, VideoModel video, String content) {
        return notificationRepository.save(new NotificationModel(user, video, content));
    }

    public void delete(NotificationModel entity) {
        notificationRepository.delete(entity);
    }

    public int notificationCount(UserModel user) {
        return notificationRepository.countByUser(user);
    }


}
