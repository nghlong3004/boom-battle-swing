package io.nghlong3004.model.request;

import lombok.NonNull;

public record BomberActionRequest(
        @NonNull
        String bomberId,
        @NonNull
        Integer keyCode,
        @NonNull
        Boolean isReleased
) {}
