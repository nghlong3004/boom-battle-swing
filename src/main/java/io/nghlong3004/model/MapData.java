package io.nghlong3004.model;

import io.nghlong3004.model.type.MapType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapData {
    private MapType type;
    private int[][] data;
}
