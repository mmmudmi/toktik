package com.scalable.toktik.record.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoDetailRecord(Long id, String video, String preview, String caption, Integer views, String username,
                                @JsonProperty("is_process") Boolean is_process) {
}
