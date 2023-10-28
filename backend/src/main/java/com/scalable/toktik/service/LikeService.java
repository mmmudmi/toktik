package com.scalable.toktik.service;

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
}
