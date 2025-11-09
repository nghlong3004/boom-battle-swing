package io.nghlong3004.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkMessage {
    private MessageType type;
    private String playerId;
    private Object data;
    private long timestamp;

    public NetworkMessage(MessageType type, Object data) {
        this.type = type;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
