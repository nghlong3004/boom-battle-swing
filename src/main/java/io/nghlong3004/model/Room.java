package io.nghlong3004.model;

import io.nghlong3004.model.type.MapType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Room {

    private String id;
    private String owner;
    private String name;
    private List<BomberInfo> bomberInfos;
    private Integer maxBomber;
    private MapType map;
    private List<ChatMessage> chatMessages;

}
