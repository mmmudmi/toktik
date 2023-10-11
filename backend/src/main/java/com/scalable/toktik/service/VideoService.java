package com.scalable.toktik.service;

import com.scalable.toktik.repsitory.VideoRepository;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }
}
