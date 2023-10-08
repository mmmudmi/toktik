package com.scalable.toktik.record.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRecord(Long id, String username, String slug, String email,
                         @JsonProperty("is_staff") Boolean is_staff) {
}
