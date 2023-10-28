package com.scalable.toktik.model;


import com.scalable.toktik.model.customid.LikeID;
import com.scalable.toktik.model.extend.CreatedTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
@IdClass(LikeID.class)
@NoArgsConstructor
public class CommentModel extends CreatedTime {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @Id
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
