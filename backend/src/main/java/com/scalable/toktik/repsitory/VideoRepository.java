package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoModel, Long> {

    Iterable<VideoModel> findAllByUser(UserModel user);

    Optional<VideoModel> findById(Long id);

    VideoModel findByVideo(String video);
}
