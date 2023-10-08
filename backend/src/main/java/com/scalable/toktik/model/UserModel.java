package com.scalable.toktik.model;

import com.scalable.toktik.model.extend.AbstractModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserModel extends AbstractModel {

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    //Reverse relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<VideoModel> videos;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<CommentModel> comments;

    public UserModel(String username, String slug, String email, String password) {
        this.username = username;
        this.slug = slug;
        this.email = email;
        this.password = password;
    }
}