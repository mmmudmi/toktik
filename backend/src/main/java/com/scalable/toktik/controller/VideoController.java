package com.scalable.toktik.controller;

import com.scalable.toktik.model.VideoModel;
import com.scalable.toktik.record.response.BoolResponse;
import com.scalable.toktik.record.s3.S3CompleteForm;
import com.scalable.toktik.record.s3.S3RequestForm;
import com.scalable.toktik.record.video.VideoRecordTool;
import com.scalable.toktik.record.video.VideoSimpleRecord;
import com.scalable.toktik.redis.RedisService;
import com.scalable.toktik.s3.AwsS3Service;
import com.scalable.toktik.service.UserService;
import com.scalable.toktik.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/video")
public class VideoController {

    private final AwsS3Service awsS3Service;
    private final RedisService redisService;
    private final VideoService videoService;
    private final UserService userService;
    private final String queueName = "video_queue";
    @Value("${aws.bucketName}")
    private String bucketName;

    public VideoController(AwsS3Service awsS3Service, RedisService redisService, VideoService videoService, UserService userService) {
        this.awsS3Service = awsS3Service;
        this.redisService = redisService;
        this.videoService = videoService;
        this.userService = userService;
    }

    @GetMapping("/upload-url/{extension}")
    public BoolResponse generateUploadUrl(@PathVariable String extension) {
        if (extension.isBlank()) {
            return new BoolResponse(false, "Extension variable is require");
        }
        return new BoolResponse(true, awsS3Service.generateNewUploadUrl(extension, bucketName, 30));
    }

    @PostMapping("/submit")
    public BoolResponse uploadComplete(S3CompleteForm s3CompleteForm, @AuthenticationPrincipal UserDetails userDetails) {
        videoService.createVideo(s3CompleteForm.filename(), s3CompleteForm.caption(), userService.findByUsername(userDetails.getUsername()));
        redisService.sendMessageToQueue(queueName, s3CompleteForm.filename());
        return new BoolResponse(true, "Successfully recorded");
    }

    @GetMapping("/delete/{filename}")
    public BoolResponse deleteRequest(@PathVariable String filename, @AuthenticationPrincipal UserDetails userDetails) {
        VideoModel video = videoService.findByVideo(filename);
        if (!video.getUser().getId().equals(userService.findByUsername(userDetails.getUsername()).getId())) {
            return new BoolResponse(false, "You don't have access to this video");
        }
        awsS3Service.deletedObject(bucketName, video);
        return new BoolResponse(true, "Successfully deleted");
    }

    @GetMapping("/latest")
    public List<VideoSimpleRecord> getLatest(@RequestParam(defaultValue = "0", required = false) Integer page,
                                             @RequestParam(defaultValue = "20", required = false) Integer size,
                                             @RequestParam(defaultValue = "desc", required = false) String order) {
        boolean isDesc = order.startsWith("desc");
        return VideoRecordTool.createSimeplRecordList(videoService.getLatest(page, size, isDesc));
    }

    @PostMapping("/playlist-access")
    public BoolResponse requestPresignURL(S3RequestForm requestForm) {
        if (requestForm.filename().endsWith("m3u8")) {
            // TODO: FInish implement
            return new BoolResponse(true, "playlist here");
        } else {
            return new BoolResponse(false, "Wrong file format");
        }
    }
}
