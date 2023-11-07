package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<NotificationModel, Long> {
    Integer countByUser(UserModel user);

    List<NotificationModel> findAllByVideo(VideoModel video, Pageable page);
}
