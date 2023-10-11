package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class AwsS3Service {
    private final AmazonS3 amazonS3;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String generatePreSignedUrl(HttpMethod httpMethod, String filePath, String bucketName, int validMins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, validMins); //validity of 10 minutes
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }

    public Integer getObjectList(String bucketName) {
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName);
        return amazonS3.listObjectsV2(request).getObjectSummaries().size();
    }
}