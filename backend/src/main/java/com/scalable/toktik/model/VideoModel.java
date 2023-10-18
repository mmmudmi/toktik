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
@Table(name = "video")
@NoArgsConstructor
public class VideoModel extends AbstractModel {
    @Column(name = "video", nullable = false) //url
    private String video;
    @Column(name = "preview", nullable = false) //url
    private String preview;
    @Column(name = "caption")
    private String caption;
    @Column(name = "views")
    private Integer views = 0;
    @Column(name = "is_process")
    private Boolean process = false;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentModel> comment;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    private Set<UserModel> users;

    public VideoModel(String video, String preview, String caption, Integer views, Boolean process, UserModel user) {
        this.video = video;
        this.preview = preview;
        this.caption = caption;
        this.views = views;
        this.process = process;
        this.user = user;
    }

    public VideoModel(String video, String caption, UserModel user) {
        this.video = video;
        this.preview = video;
        this.caption = caption;
        this.user = user;
    }
}
