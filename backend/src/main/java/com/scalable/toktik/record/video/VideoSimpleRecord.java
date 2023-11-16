package com.scalable.toktik.record.video;

import java.time.LocalDateTime;

public record VideoSimpleRecord(Long id, String video, String preview, String caption, Integer views, String username,
                                Integer like_count, Integer dislike_count, Integer comment_count,
                                LocalDateTime created) {
}
