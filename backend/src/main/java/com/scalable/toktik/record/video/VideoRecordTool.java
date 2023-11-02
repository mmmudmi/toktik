package com.scalable.toktik.record.video;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.comment.CommentRecordTool;
import com.scalable.toktik.s3.AwsS3Service;
import com.scalable.toktik.service.CommentService;
import com.scalable.toktik.service.DislikeService;
import com.scalable.toktik.service.LikeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoRecordTool {

    private final AwsS3Service awsS3Service;
    private final CommentService commentService;
    private final LikeService likeService;
    private final DislikeService dislikeService;
    @Value("${aws.bucketName}")
    private String bucketName;

    public VideoRecordTool(AwsS3Service awsS3Service, CommentService commentService, LikeService likeService, DislikeService dislikeService) {
        this.awsS3Service = awsS3Service;
        this.commentService = commentService;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
    }

    public VideoSimpleRecord createVideoSimpleRecord(VideoModel video, UserModel user) {
        Integer likeCount = likeService.likeCount(video);
        Integer dislikeCount = dislikeService.dislikeCount(video);
        Integer commentCount = commentService.commentCount(video);
        boolean isLike;
        if (user != null) {
            isLike = likeService.isLike(video, user);
        } else {
            isLike = false;
        }
        return new VideoSimpleRecord(video.getId(), video.getVideo(), presignTool(video.getPreview()), video.getCaption(),
                video.getViews(), video.getUser().getUsername(), likeCount, dislikeCount, commentCount, isLike, video.getCreated());
    }

    public List<VideoSimpleRecord> createVideoSimepleRecordList(List<VideoModel> videos, UserModel user) {
        return videos.stream().map(video -> this.createVideoSimpleRecord(video, user)).toList();
    }

    public VideoUserSimpleRecord createVideoUserSimpleRecord(VideoModel video) {
        Integer likeCount = likeService.likeCount(video);
        Integer dislikeCount = dislikeService.dislikeCount(video);
        Integer commentCount = commentService.commentCount(video);
        return new VideoUserSimpleRecord(video.getId(), video.getVideo(),presignTool(video.getPreview()), video.getCaption(),
                video.getViews(), video.getUser().getUsername(), likeCount, dislikeCount, commentCount, video.getStatus(), video.getCreated());
    }

    public List<VideoUserSimpleRecord> createVideoUserSimepleRecordList(List<VideoModel> videos) {
        return videos.stream().map(this::createVideoUserSimpleRecord).toList();
    }

    public VideoDetailRecord createDetailRecord(VideoModel video, UserModel user) {
        Integer likeCount = likeService.likeCount(video);
        Integer dislikeCount = dislikeService.dislikeCount(video);
        Integer commentCount = commentService.commentCount(video);
        boolean isLike = likeService.isLike(video, user);
        return new VideoDetailRecord(video.getId(), video.getVideo(), presignTool(video.getPreview()), video.getCaption(),
                video.getViews(), video.getUser().getUsername(), likeCount, dislikeCount, commentCount, isLike, video.getCreated(),
                CommentRecordTool.createcommentRecordList(video.getComments()));
    }

    public List<VideoDetailRecord> createDetailRecordList(List<VideoModel> videos, UserModel user) {
        return videos.stream().map(video -> this.createDetailRecord(video, user)).toList();
    }

    private String presignTool(String file) {
        return awsS3Service.generatePreSignedUrl(HttpMethod.GET, file, bucketName, 30);
    }
}
