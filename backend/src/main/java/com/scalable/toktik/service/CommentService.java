package com.scalable.toktik.service;

import com.scalable.toktik.model.CommentModel;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.repsitory.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentModel createComment(UserModel user, VideoModel video, String comment) {
        return commentRepository.save(new CommentModel(user, video, comment));
    }

    public void delete(CommentModel entity) {
        commentRepository.delete(entity);
    }

    public int commentCount(VideoModel video) {
        return commentRepository.countByVideo(video);
    }


}
