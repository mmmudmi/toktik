package com.scalable.toktik.record.video;

import java.time.LocalDateTime;

public record VideoSlimRecord(Long id, String video, String preview, String caption, LocalDateTime created) {
}
