package io.nghlong3004.model.response;

import io.nghlong3004.model.type.ItemType;
import lombok.NonNull;

public record StartGameResponse(
        @NonNull
        MapDataResponse mapData,
        @NonNull
        ItemType[][] itemSpawns
) {}
