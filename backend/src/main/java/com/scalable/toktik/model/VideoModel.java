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
@Table(name = "movies")
@NoArgsConstructor
public class VideoModel extends AbstractModel {
    @Column(name = "title")
    private String title;
    @Column(name = "video", nullable = false) //url
    private String video;
    @Column(name = "preview", nullable = false) //url
    private String preview;
    @Column(name = "summary", columnDefinition = "mediumtext")
    private String details;
    @Column(name = "views")
    private Integer views = 0;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentModel> reviews;

    public VideoModel(String title, String video, String preview, String details, Integer views, UserModel user) {
        this.title = title;
        this.video = video;
        this.preview = preview;
        this.details = details;
        this.views = views;
        this.user = user;
    }
}
