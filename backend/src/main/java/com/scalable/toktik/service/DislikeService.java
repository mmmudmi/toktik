package com.scalable.toktik.service;

import com.scalable.toktik.model.DislikeModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.DislikeRepository;
import org.springframework.stereotype.Service;

@Service
public class DislikeService {
    private final DislikeRepository dislikeRepository;

    public DislikeService(DislikeRepository dislikeRepository) {
        this.dislikeRepository = dislikeRepository;
    }

    public boolean isDislike(VideoModel video, UserModel user) {
        return dislikeRepository.existsByVideoAndUser(video, user);
    }

    public int dislikeCount(VideoModel video) {
        return dislikeRepository.countByVideo(video);
    }

    public DislikeModel find(VideoModel video, UserModel user) {
        return dislikeRepository.findByVideoAndUser(video, user);
    }

    public void create(VideoModel video, UserModel user) {
        dislikeRepository.save(new DislikeModel(user, video));
    }

    public void delete(DislikeModel dislikeModel) {
        dislikeRepository.delete(dislikeModel);
    }

    public boolean dislike(VideoModel video, UserModel user) {
        /** @return new like value after process */
        if (isDislike(video, user)) {
            dislikeRepository.delete(find(video, user));
            return false;
        }
        create(video, user);
        return true;
    }
}
