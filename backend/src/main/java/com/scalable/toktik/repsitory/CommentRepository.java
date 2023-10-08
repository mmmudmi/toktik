package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.CommentModel;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentModel, Long> {
}
