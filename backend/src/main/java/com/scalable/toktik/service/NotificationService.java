package com.scalable.toktik.service;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.NotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<NotificationModel> latestNotification(UserModel user, Integer page, Integer size) {
        return notificationRepository.findAllByUserAndRead(user, false, PageRequest.of(page, size, Sort.by("created").descending()));
    }

    public int notificationCount(UserModel user) {
        return notificationRepository.countByUser(user);
    }

    public void readAll(UserModel user) {
        notificationRepository.setAllRead(user);
    }


}
