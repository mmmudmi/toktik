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
        return new VideoSimpleRecord(video.getVideo(), awsS3Service.generatePreSignedUrl(HttpMethod.GET, video.getPreview(), bucketName, 30), video.getCaption(), video.getViews(),video.getUser().getUsername());
    }

    public List<VideoSimpleRecord> createSimeplRecordList(List<VideoModel> videos) {
        return videos.stream().map(this::createSimpleRecord).toList();
    }
}
