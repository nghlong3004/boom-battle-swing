package io.nghlong3004.websocket;

import com.google.gson.Gson;
import io.nghlong3004.websocket.model.NetworkMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    private final Gson gson;
    private final ConcurrentLinkedQueue<NetworkMessage> messageQueue;
    private boolean connected;
    @Getter
    @Setter
    private String playerId;

    public WebSocketClient(URI serverUri) {
        super(serverUri);
        this.gson = new Gson();
        this.messageQueue = new ConcurrentLinkedQueue<>();
        this.connected = false;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("Connected to server: {}", getURI());
        connected = true;
    }

    @Override
    public void onMessage(String message) {
        try {
            NetworkMessage networkMessage = gson.fromJson(message, NetworkMessage.class);
            messageQueue.offer(networkMessage);
            log.debug("Received message: {}", networkMessage.getType());
        } catch (Exception e) {
            log.error("Error parsing message: {}", message, e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("Connection closed: {} - {}", code, reason);
        connected = false;
    }

    @Override
    public void onError(Exception ex) {
        log.error("WebSocket error", ex);
        connected = false;
    }

    public void sendMessage(NetworkMessage message) {
        if (connected && isOpen()) {
            message.setPlayerId(playerId);
            message.setTimestamp(System.currentTimeMillis());
            String json = gson.toJson(message);
            send(json);
            log.debug("Sent message: {}", message.getType());
        }
        else {
            log.warn("Cannot send message, not connected");
        }
    }

    public NetworkMessage pollMessage() {
        return messageQueue.poll();
    }

    public boolean hasMessages() {
        return !messageQueue.isEmpty();
    }

    public boolean isConnected() {
        return connected && isOpen();
    }

}
