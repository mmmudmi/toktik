package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.VideoModel;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<VideoModel, Long> {
}
