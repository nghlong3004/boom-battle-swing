package io.nghlong3004.model.request;

import lombok.NonNull;

public record ChatMessageRequest(
        @NonNull
        String roomId,
        @NonNull
        String owner,
        @NonNull
        String content
) {}
