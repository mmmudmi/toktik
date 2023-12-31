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
@Table(name = "likes")
@IdClass(LikeID.class)
@NoArgsConstructor
public class LikeModel extends LikeAbstractModel {

    public LikeModel(UserModel user, VideoModel video) {
        this.setUser(user);
        this.setVideo(video);
    }
}
