package com.scalable.toktik.record.response;

import java.util.List;

public record ObjectResponse<T>(Boolean success, String message, List<T> records) {
}
