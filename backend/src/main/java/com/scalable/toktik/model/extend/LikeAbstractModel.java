package com.scalable.toktik.model.extend;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.model.customid.LikeID;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@MappedSuperclass
@IdClass(LikeID.class)
public class LikeAbstractModel {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserModel user;
    @Id
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false, updatable = false)
    private VideoModel video;
}
