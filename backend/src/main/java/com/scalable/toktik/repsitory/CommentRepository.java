package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.CommentModel;
import com.scalable.toktik.model.VideoModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<CommentModel, Long> {
    Integer countByVideo(VideoModel video);

    List<CommentModel> findAllByVideo(VideoModel video, Pageable page);
}
