package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends PagingAndSortingRepository<VideoModel, Long> {

    List<VideoModel> findAllByUser(UserModel user, Pageable page);


    Optional<VideoModel> findById(Long id);

    Optional<VideoModel> findByVideo(String video);

    Optional<VideoModel> findVideoModelByVideoStartsWith(String filename);

    List<VideoModel> findAllByProcess(Boolean is_process, Pageable page);
}

