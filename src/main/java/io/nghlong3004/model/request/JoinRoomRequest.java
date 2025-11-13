package io.nghlong3004.model.request;

import io.nghlong3004.model.BomberInfo;
import lombok.NonNull;

public record JoinRoomRequest(
        @NonNull
        String id,
        @NonNull
        BomberInfo bomberInfo
) {}
