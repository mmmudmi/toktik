package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.NotificationModel;
import com.scalable.toktik.model.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<NotificationModel, Long> {
    Integer countByUser(UserModel user);

    List<NotificationModel> findAllByUser(UserModel user, Pageable page);

    List<NotificationModel> findAllByUserAndRead(UserModel user, Boolean is_read, Pageable page);

    Integer countByUserAndRead(UserModel user, Boolean is_read);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationModel noti SET noti.read = true WHERE noti.user = ?1 AND noti.read = false")
    void setAllRead(UserModel user);
}
