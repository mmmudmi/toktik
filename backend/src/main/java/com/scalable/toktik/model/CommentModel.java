package com.scalable.toktik.model;


import com.scalable.toktik.model.extend.AbstractModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class CommentModel extends AbstractModel {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false, updatable = false)
    private VideoModel video;
    @Column(name = "comment", columnDefinition = "mediumtext")
    private String comment;

    public CommentModel(UserModel user, VideoModel video, String comment) {
        this.user = user;
        this.video = video;
        this.comment = comment;
    }
}
