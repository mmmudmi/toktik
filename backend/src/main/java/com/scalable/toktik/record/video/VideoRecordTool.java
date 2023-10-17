package com.scalable.toktik.record.video;

import com.scalable.toktik.model.VideoModel;

import java.util.List;

public class VideoRecordTool {

    public static VideoSimpleRecord createSimpleRecord(VideoModel video) {
        return new VideoSimpleRecord(video.getVideo(), video.getPreview(), video.getCaption(), video.getViews());
    }

    public static List<VideoSimpleRecord> createSimeplRecordList(List<VideoModel> videos) {
        return videos.stream().map(VideoRecordTool::createSimpleRecord).toList();
    }
}
