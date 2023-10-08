package com.scalable.toktik.repsitory;

import com.scalable.toktik.model.UserModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByEmail(String email);

    UserModel findByUsername(String username);

    UserModel findByPasswordEndingWith(String keyword);

    UserModel findBySlug(String slug);

    boolean existsBySlug(String slug);
}
