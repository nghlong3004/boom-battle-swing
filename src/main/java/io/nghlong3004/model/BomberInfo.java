package io.nghlong3004.model;

import io.nghlong3004.model.type.SkinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BomberInfo {

    private String id;
    private String name;
    private SkinType skin;
    private boolean isReady;

}
