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

    public void createComment(UserModel user, VideoModel video, String comment) {
        commentRepository.save(new CommentModel(user, video, comment));
    }

    public CommentModel findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void delete(CommentModel entity) {
        commentRepository.delete(entity);
    }

    public Iterable<CommentModel> getAll() {
        return commentRepository.findAll();
    }

}
