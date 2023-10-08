package com.scalable.toktik.service;

import com.github.slugify.Slugify;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.repsitory.UserRepository;
import com.scalable.toktik.util.Slugifier;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String email, String password) {
        createUser(username, email, password, false);
    }

    public void createUser(String username, String email, String password, Boolean isStaff) {
        Slugify slugify = Slugifier.getInstance();
        String slug = slugify.slugify(username);
        while (true) {
            if (repository.existsBySlug(slug)) {
                slug = slugify.slugify(slug.concat(RandomString.hashOf(4)));
            } else {
                break;
            }
        }
        repository.save(new UserModel(username, slug, email, passwordEncoder.encode(password), isStaff));
    }

    public void delete(UserModel entity) {
        repository.delete(entity);
    }

    public void update(UserModel model, String username, String email) {
        Slugify slugify = Slugifier.getInstance();
        String slug = slugify.slugify(username);
        if (!slug.equals(model.getSlug())) {
            while (true) {
                if (repository.existsBySlug(slug)) {
                    slug = slugify.slugify(slug.concat(RandomString.hashOf(4)));
                } else {
                    break;
                }
            }
            model.setSlug(slug);
        }
        model.setUsername(username);
        model.setEmail(email);
        repository.save(model);
    }

    public boolean checkPassword(UserModel user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void newPassword(UserModel user, String password) {
        String hash = passwordEncoder.encode(password);
        user.setPassword(hash);
        repository.save(user);
    }

    public UserModel findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserModel findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public UserModel findByToken(String token) {
        return repository.findByPasswordEndingWith(token);
    }

    public UserModel findBySlug(String slug) {
        return repository.findBySlug(slug);
    }

    public Iterable<UserModel> getAll() {
        return repository.findAll();
    }
}
