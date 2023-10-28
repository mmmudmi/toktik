package com.scalable.toktik.record.video;

import java.time.LocalDateTime;

public record VideoUserSimpleRecord(Long id, String video, String preview, String caption, Integer views,
                                    String username, Integer like_count, Integer comment_count, Boolean is_process,
                                    LocalDateTime created) {
}
