package com.scalable.toktik.service;

import com.scalable.toktik.model.LikeModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public boolean isLike(VideoModel video, UserModel user) {
        return likeRepository.existsByVideoAndUser(video, user);
    }

    public int likeCount(VideoModel video) {
        return likeRepository.countByVideo(video);
    }

    public LikeModel find(VideoModel video, UserModel user) {
        return likeRepository.findByVideoAndUser(video, user);
    }

    public LikeModel create(VideoModel video, UserModel user) {
        return likeRepository.save(new LikeModel(user, video));
    }

    public void delete(LikeModel likeModel) {
        likeRepository.delete(likeModel);
    }

    public boolean like(VideoModel video, UserModel user) {
        /** @return new like value after process */
        if (isLike(video, user)) {
            likeRepository.delete(find(video, user));
            return false;
        }
        create(video, user);
        return true;
    }
}
