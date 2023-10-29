package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.LikeModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.model.customid.LikeID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LikeRepository extends PagingAndSortingRepository<LikeModel, LikeID> {
    Integer countByVideo(VideoModel video);

    Boolean existsByVideoAndUser(VideoModel video, UserModel user);

    LikeModel findByVideoAndUser(VideoModel video, UserModel user);
}
