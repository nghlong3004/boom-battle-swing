package io.nghlong3004.websocket;

import com.google.gson.Gson;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.ChatMessage;
import io.nghlong3004.model.NetworkMessage;
import io.nghlong3004.model.Room;
import io.nghlong3004.model.request.BomberActionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MessageHandler {

    private final NetworkManager networkManager;
    private final Gson gson = new Gson();

    public void handle(NetworkMessage message) {
        log.debug("Handling message: {}", message.getType());

        switch (message.getType()) {
            case CONNECT -> handleConnect(message);
            case CREATE_ROOM -> handleCreateRoom(message);
            case ROOM_LIST -> handleRoomList(message);
            case JOIN_ROOM -> handleJoinRoom(message);
            case ROOM_UPDATE -> handleRoomUpdate(message);
            case LEAVE_ROOM -> handleLeaveRoom(message);
            case CHAT_MESSAGE -> handleChatMessage(message);
            case START_GAME -> handleStartGame(message);
            case BOMBER_ACTION -> handleBomberAction(message);
            default -> log.warn("Unhandled message type: {}", message.getType());
        }
    }

    private void handleBomberAction(NetworkMessage message) {
        var bomberActionRequest = gson.fromJson(message.getData(), BomberActionRequest.class);
        networkManager.keyAction(bomberActionRequest.keyCode(), bomberActionRequest.bomberId(),
                                 bomberActionRequest.isReleased());
    }

    private void handleLeaveRoom(NetworkMessage message) {
        networkManager.setCurrentRoom(null);
    }

    private void handleJoinRoom(NetworkMessage message) {
        if (message.getData()
                   .isEmpty() || message.getData()
                                        .isBlank()) {
            return;
        }
        var room = gson.fromJson(message.getData(), Room.class);
        networkManager.setCurrentRoom(room);
    }

    private void handleCreateRoom(NetworkMessage message) {
        var room = gson.fromJson(message.getData(), Room.class);
        networkManager.setCurrentRoom(room);
    }

    private void handleConnect(NetworkMessage message) {
        networkManager.setBomberId(message.getData());
        log.info("Bomber ID assigned: {}", message.getData());
    }

    private void handleRoomList(NetworkMessage message) {
        var rooms = gson.fromJson(message.getData(), Room[].class);
        if (networkManager.getAvailableRooms() == null) {
            networkManager.setAvailableRooms(new ArrayList<>());
        }
        networkManager.getAvailableRooms()
                      .clear();
        networkManager.getAvailableRooms()
                      .addAll(List.of(rooms));
        log.info("Received {} rooms", networkManager.getAvailableRooms()
                                                    .size());
    }

    private void handleRoomUpdate(NetworkMessage message) {
        var room = gson.fromJson(message.getData(), Room.class);
        networkManager.setCurrentRoom(room);
        log.info("room updated: name = {}, id = {}", room.getName(), room.getId());
    }

    private void handleChatMessage(NetworkMessage message) {
        var chatMessage = gson.fromJson(message.getData(), ChatMessage.class);
        networkManager.getCurrentRoom()
                      .getChatMessages()
                      .add(chatMessage);
        log.debug("Chat message from {}: {}", chatMessage.getOwner(), chatMessage.getContent());
    }

    private void handleStartGame(NetworkMessage message) {
        networkManager.setPlaying(true);
        log.info("Game starting!");
    }

    private void handleGameStateUpdate(NetworkMessage message) {
        log.debug("Game state updated");
    }

    private void handleError(NetworkMessage message) {
        log.error("Server error: {}", message.getData());
    }

}
