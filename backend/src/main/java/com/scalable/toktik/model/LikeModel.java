package com.scalable.toktik.model;

import com.scalable.toktik.model.customid.LikeID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "likes")
@IdClass(LikeID.class)
@NoArgsConstructor
public class LikeModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @Id
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false, updatable = false)
    private VideoModel video;
}
