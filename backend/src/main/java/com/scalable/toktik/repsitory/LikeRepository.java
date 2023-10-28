package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.LikeModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LikeRepository extends PagingAndSortingRepository<LikeModel, Long> {
    Integer countByVideo(VideoModel video);

    Boolean existsByVideoAndUser(VideoModel video, UserModel user);
}
