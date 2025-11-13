package io.nghlong3004.model.request;

import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.SkinType;
import lombok.NonNull;

public record CreateRoomRequest(
        @NonNull
        String owner,
        @NonNull
        String ownerName,
        @NonNull
        String name,
        @NonNull
        Integer maxBombers,
        @NonNull
        MapType mapType,
        @NonNull
        SkinType skinType
) {}
