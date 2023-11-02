package com.scalable.toktik.model;

import com.scalable.toktik.model.extend.AbstractModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "video")
@NoArgsConstructor
public class VideoModel extends AbstractModel {
    @Column(name = "video", nullable = false, unique = true) //url
    private String video;
    @Column(name = "preview", nullable = false) //url
    private String preview;
    @Column(name = "caption")
    private String caption;
    @Column(name = "views")
    private Integer views = 0;
    @Column(name = "status", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.IntegerType")
    private Integer status = 0;
    // FK Relation
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    // Reverse Relation
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private Set<CommentModel> comments;
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private Set<LikeModel> likes;

    // M2M relation
    public VideoModel(String video, String preview, String caption, Integer views, Integer status, UserModel user) {
        this.video = video;
        this.preview = preview;
        this.caption = caption;
        this.views = views;
        this.status = status;
        this.user = user;
    }

    public VideoModel(String video, String caption, UserModel user) {
        this.video = video;
        this.preview = video;
        this.caption = caption;
        this.user = user;
    }
}
