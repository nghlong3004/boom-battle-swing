package io.nghlong3004.model;

import io.nghlong3004.model.type.MessageType;
import lombok.Data;

@Data
public class NetworkMessage {

    private MessageType type;
    private String data;

    public NetworkMessage(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

}
