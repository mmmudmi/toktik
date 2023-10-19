package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.service.VideoService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        calendar.add(Calendar.MINUTE, validMins); //validity of n minutes
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }

    public String generateNewUploadUrl(String extension, String bucketName, int validMins) {
        String file = UUID.randomUUID() + "." + extension.strip().toLowerCase();
        return generatePreSignedUrl(HttpMethod.PUT, file, bucketName, validMins);
    }

    public void deletedObject(String bucketName, VideoModel video) {
        String filename = video.getVideo().substring(0, video.getVideo().indexOf('.'));
        String[] keys = amazonS3.listObjectsV2(bucketName, filename).getObjectSummaries().stream().map(S3ObjectSummary::getKey).toList().toArray(new String[0]);
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
        amazonS3.deleteObjects(request);
        videoService.delete(video);
    }

    public List<String> getObjectList(String bucketName) {
        return amazonS3.listObjectsV2(bucketName)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public String downloadPlaylist(String filePath, String bucketName) {
        System.out.println(filePath);
        S3Object s3Object = amazonS3.getObject(bucketName, filePath);
        StringBuilder content = new StringBuilder();
        try (InputStream objectData = s3Object.getObjectContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(objectData))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            // Handle the exception
            return "";
        }
    }
}