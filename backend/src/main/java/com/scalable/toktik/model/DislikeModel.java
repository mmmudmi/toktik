package com.scalable.toktik.model;

import com.scalable.toktik.model.customid.LikeID;
import com.scalable.toktik.model.extend.LikeAbstractModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "dislikes")
@IdClass(LikeID.class)
@NoArgsConstructor
public class DislikeModel extends LikeAbstractModel {

    public DislikeModel(UserModel user, VideoModel video) {
        this.setUser(user);
        this.setVideo(video);
    }
}
