package com.scalable.toktik.model;

import com.scalable.toktik.model.extend.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
public class NotificationModel extends AbstractModel {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false, updatable = false)
    private VideoModel video;
    @Column(name = "content", columnDefinition = "mediumtext")
    private String content;
    @Column(name = "is_read")
    private boolean read = false;

    public NotificationModel(UserModel user, VideoModel video, String content) {
        this.setUser(user);
        this.setVideo(video);
        this.setContent(content);
    }
}
