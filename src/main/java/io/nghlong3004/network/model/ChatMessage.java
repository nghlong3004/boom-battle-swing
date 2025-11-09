package io.nghlong3004.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String playerId;
    private String playerName;
    private String message;
    private long timestamp;

    public ChatMessage(String playerName, String message) {
        this.playerName = playerName;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
