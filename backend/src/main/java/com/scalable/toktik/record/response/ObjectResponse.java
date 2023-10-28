package com.scalable.toktik.record.response;

public record ObjectResponse<T>(Boolean success, String message, T records) {
}
