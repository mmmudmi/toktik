package com.scalable.toktik.record.video;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.s3.AwsS3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoRecordTool {

    private final AwsS3Service awsS3Service;
    @Value("${aws.bucketName}")
    private String bucketName;

    public VideoRecordTool(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    public VideoSimpleRecord createSimpleRecord(VideoModel video) {

        return new VideoSimpleRecord(video.getVideo(), presignTool(video.getPreview()), video.getCaption(), video.getViews(), video.getUser().getUsername());
    }

    public List<VideoSimpleRecord> createSimepleRecordList(List<VideoModel> videos) {
        return videos.stream().map(this::createSimpleRecord).toList();
    }

    public VideoDetailRecord createDetailRecord(VideoModel video) {
        return new VideoDetailRecord(video.getVideo(), presignTool(video.getPreview()), video.getCaption(), video.getViews(), video.getUser().getUsername(), video.getProcess());
    }

    public List<VideoDetailRecord> createDetailRecordList(List<VideoModel> videos) {
        return videos.stream().map(this::createDetailRecord).toList();
    }

    private String presignTool(String file) {
        return awsS3Service.generatePreSignedUrl(HttpMethod.GET, file, bucketName, 30);
    }
}
