package com.scalable.toktik.service;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<VideoModel> findAllByUser(UserModel user) {
        return videoRepository.findAllByUserOrderByCreatedDesc(user);
    }

    public void delete(VideoModel video) {
        videoRepository.delete(video);
    }

    public VideoModel findVideoStartWith(String filename) {
        return videoRepository.findVideoModelByVideoStartsWith(filename);
    }

    public List<VideoModel> getLatest(int page, int size, boolean desc) {
        if (desc) {
            return videoRepository.findAllByIs_process(true, PageRequest.of(page, size, Sort.by("created").descending()));
        } else {
            return videoRepository.findAllByIs_process(true, PageRequest.of(page, size, Sort.by("created").ascending()));
        }
    }
}
