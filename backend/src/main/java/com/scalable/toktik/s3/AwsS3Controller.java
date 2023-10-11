package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.record.response.BoolResponse;
import com.scalable.toktik.record.s3.S3CompleteForm;
import com.scalable.toktik.record.s3.S3RequestForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/generate-upload-url/{extension}")
    public BoolResponse generateUploadUrl(@PathVariable String extension) {
        if (extension.isBlank()) {
            return new BoolResponse(false, "Extension variable is require");
        }
        String signURL = awsS3Service.generatePreSignedUrl(HttpMethod.PUT, UUID.randomUUID() + "." + extension.strip().toLowerCase(), bucketName, 30);
        return new BoolResponse(true, signURL);
    }

    @GetMapping("/count")
    public BoolResponse count() {
        return new BoolResponse(true, awsS3Service.getObjectList(bucketName).toString());
    }


    @PostMapping("/upload-complete")
    public BoolResponse uploadComplete(S3CompleteForm s3CompleteForm) {
        return new BoolResponse(true, "Successfully recorded");
    }

    @PostMapping("/access-request")
    public BoolResponse accessRequest(S3RequestForm s3RequestForm) {
        return new BoolResponse(true, awsS3Service.generatePreSignedUrl(HttpMethod.GET, s3RequestForm.filename(), bucketName, 30));
    }
}



