package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.record.response.BoolResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;
    @Value("${aws.bucketName}")
    private String bucketName;

    @Autowired
    public AwsS3Controller(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @GetMapping("/generate-upload-url")
    public BoolResponse generateUploadUrl() {
        return new BoolResponse(true, awsS3Service.generatePreSignedUrl(HttpMethod.PUT, UUID.randomUUID() + ".txt", bucketName, 30));
    }
}



