package com.scalable.toktik.record.response;

import java.util.List;

public record ObjectResponse(Boolean success, String message, List<Record> records) {
}
