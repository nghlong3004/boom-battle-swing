package io.nghlong3004.model.response;

import lombok.NonNull;

import java.util.List;

public record MapDataResponse(
        @NonNull
        int[][] map,
        @NonNull
        List<PointResponse> spawns
) {}

