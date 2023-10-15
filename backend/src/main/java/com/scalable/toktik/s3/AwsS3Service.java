package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3Service {
    private final AmazonS3 amazonS3;
    private final VideoService videoService;

    public AwsS3Service(AmazonS3 amazonS3, VideoService videoService) {
        this.amazonS3 = amazonS3;
        this.videoService = videoService;
    }

    public String generatePreSignedUrl(HttpMethod httpMethod, String filePath, String bucketName, int validMins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, validMins); //validity of 10 minutes
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }

    public void deletedObject(String bucketName, VideoModel video) {
        String filename = video.getVideo().substring(0, video.getVideo().indexOf('.'));
        String[] keys = amazonS3.listObjectsV2(bucketName, filename).getObjectSummaries().stream().map(S3ObjectSummary::getKey).toList().toArray(new String[0]);
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
        amazonS3.deleteObjects(request);
        videoService.delete(video);
    }

    public List<String> getObjectList(String bucketName) {
        return amazonS3.listObjectsV2(bucketName, "1f0b6854-de54-4bac-b3c3-8b90f3e321de")
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}