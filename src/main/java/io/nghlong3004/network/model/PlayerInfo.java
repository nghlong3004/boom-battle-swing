package io.nghlong3004.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInfo {
    private String playerId;
    private String playerName;
    private String skinType;
    private boolean isReady;
    private boolean isHost;
}
