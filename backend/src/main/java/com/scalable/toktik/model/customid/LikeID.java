package com.scalable.toktik.model.customid;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(force = true)
public class LikeID implements Serializable {

    private final Long user;
    private final Long video;

    public LikeID(UserModel user, VideoModel video) {
        this.user = user.getId();
        this.video = video.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeID likeID)) return false;
        return Objects.equals(user, likeID.user) && Objects.equals(video, likeID.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, video);
    }
}
