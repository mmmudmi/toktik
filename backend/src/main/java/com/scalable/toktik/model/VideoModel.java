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
    @Column(name = "title")
    private String title;
    @Column(name = "video", nullable = false) //url
    private String video;
    @Column(name = "preview", nullable = false) //url
    private String preview;
    @Column(name = "description", columnDefinition = "mediumtext")
    private String description;
    @Column(name = "views")
    private Integer views = 0;
    @Column(name = "is_process")
    private Boolean is_process;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<CommentModel> reviews;

    public VideoModel(String title, String video, String preview, String description, Integer views, Boolean is_process, UserModel user) {
        this.title = title;
        this.video = video;
        this.preview = preview;
        this.description = description;
        this.views = views;
        this.user = user;
    }
}
