package com.scalable.toktik.service;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void createVideo(String filename, String caption, UserModel user) {
        videoRepository.save(new VideoModel(filename, caption, user));
    }

    public void updateVideo(VideoModel video) {
        videoRepository.save(video);
    }

    public Optional<VideoModel> findById(Long id) {
        return videoRepository.findById(id);
    }

    public VideoModel findByVideo(String filename) {
        return videoRepository.findByVideo(filename);
    }

    public Iterable<VideoModel> findAllByUser(UserModel user) {
        return videoRepository.findAllByUser(user);
    }

    public void delete(VideoModel video) {
        videoRepository.delete(video);
    }
}
