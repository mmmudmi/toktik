package com.scalable.toktik.s3;

import com.amazonaws.HttpMethod;
import com.scalable.toktik.record.response.BoolResponse;
import com.scalable.toktik.record.s3.S3CompleteForm;
import com.scalable.toktik.record.s3.S3RequestForm;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;
    private final RedisService redisService;
    private final VideoService videoService;
    private final UserService userService;
    @Value("${aws.bucketName}")
    private String bucketName;
    private final String queueName = "video_queue";

    @Autowired
    public AwsS3Controller(AwsS3Service awsS3Service, RedisService redisService, VideoService videoService, UserService userService) {
        this.awsS3Service = awsS3Service;
        this.redisService = redisService;
        this.videoService = videoService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/generate-upload-url/{extension}")
    public BoolResponse generateUploadUrl(@PathVariable String extension) {
        if (extension.isBlank()) {
            return new BoolResponse(false, "Extension variable is require");
        }
        String signURL = awsS3Service.generatePreSignedUrl(HttpMethod.PUT, UUID.randomUUID() + "." + extension.strip().toLowerCase(), bucketName, 30);
        return new BoolResponse(true, signURL);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload-complete")
    public BoolResponse uploadComplete(S3CompleteForm s3CompleteForm, @AuthenticationPrincipal UserDetails userDetails) {
        videoService.createVideo(s3CompleteForm.filename(), s3CompleteForm.caption(), userService.findByUsername(userDetails.getUsername()));
        redisService.sendMessageToQueue(queueName, s3CompleteForm.filename());
        return new BoolResponse(true, "Successfully recorded");
    }

    @GetMapping("/list")
    public BoolResponse count() {
        return new BoolResponse(true, awsS3Service.getObjectList(bucketName).toString());
    }

    @PostMapping("/access-request")
    public BoolResponse accessRequest(S3RequestForm s3RequestForm) {
        return new BoolResponse(true, awsS3Service.generatePreSignedUrl(HttpMethod.GET, s3RequestForm.filename(), bucketName, 30));
    }

    @GetMapping("/delete/{filename}")
    public BoolResponse deleteRequest(@PathVariable String filename) {
        awsS3Service.deletedObject(bucketName, videoService.findByVideo(filename));
        return new BoolResponse(true, "Successfully deleted");
    }
}



