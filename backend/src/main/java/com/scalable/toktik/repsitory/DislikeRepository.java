package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.DislikeModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.model.customid.LikeID;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DislikeRepository extends PagingAndSortingRepository<DislikeModel, LikeID> {
    Integer countByVideo(VideoModel video);

    Boolean existsByVideoAndUser(VideoModel video, UserModel user);

    DislikeModel findByVideoAndUser(VideoModel video, UserModel user);

}
