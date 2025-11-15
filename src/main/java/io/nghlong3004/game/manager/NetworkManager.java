package io.nghlong3004.game.manager;

import io.nghlong3004.game.input.BomberKeyAction;
import io.nghlong3004.model.BomberInfo;
import io.nghlong3004.model.NetworkMessage;
import io.nghlong3004.model.Room;
import io.nghlong3004.model.entities.Bomber;
import io.nghlong3004.model.request.BomberActionRequest;
import io.nghlong3004.model.request.ChatMessageRequest;
import io.nghlong3004.model.request.CreateRoomRequest;
import io.nghlong3004.model.request.JoinRoomRequest;
import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.MessageType;
import io.nghlong3004.model.type.SkinType;
import io.nghlong3004.websocket.BomberWebSocketClient;
import io.nghlong3004.websocket.MessageHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.game.input.BomberKeyAction.LOOKUP;

@Slf4j
public class NetworkManager {
    @Getter
    private BomberWebSocketClient client;
    @Getter
    private String bomberId;
    @Getter
    private String bomberName;
    @Setter
    @Getter
    private Room currentRoom;
    @Getter
    @Setter
    private List<Room> availableRooms;
    private List<Bomber> bombers;

    @Getter
    @Setter
    private boolean isPlaying;

    private final MessageHandler messageHandler;

    public NetworkManager() {
        this.availableRooms = new ArrayList<>();
        this.messageHandler = new MessageHandler(this);
        this.isPlaying = false;
    }

    public void connect(String serverUrl, String playerName) {
        this.bomberName = playerName;

        try {
            URI serverUri = new URI(serverUrl);
            client = new BomberWebSocketClient(serverUri);
            client.connectBlocking();
            log.info("Connected to server: {}", serverUrl);
        } catch (Exception e) {
            log.error("Failed to connect to server", e);
        }
    }

    public List<Bomber> getBombers() {
        if (bombers == null) {
            bombers = new ArrayList<>();
        }
        bombers.clear();
        for (var bomberInfo : this.currentRoom.getBomberInfos()) {
            var bomber = new Bomber(0, 0, bomberInfo.getSkin());
            bomber.setBomberId(bomberInfo.getId());
            bombers.add(bomber);
        }
        return this.bombers;
    }

    public void disconnect() {
        if (client != null && client.isConnected()) {
            client.close();
            log.info("Disconnected from server");
        }
    }

    public void update() {
        if (client == null || !client.isConnected()) {
            return;
        }

        while (client.hasMessages()) {
            NetworkMessage message = client.pollMessage();
            messageHandler.handle(message);
        }
    }

    public void createRoom(String roomName, int maxBombers, MapType mapType, SkinType skinType) {
        var createRoomResponse = new CreateRoomRequest(bomberId, bomberName, roomName, maxBombers, mapType, skinType);
        var message = new NetworkMessage(MessageType.CREATE_ROOM, client.getGson()
                                                                        .toJson(createRoomResponse));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; createRoom ignored (offline mode)");
        }
    }

    public void joinRoom(String roomId) {
        BomberInfo bomberInfo = new BomberInfo(bomberId, bomberName, SkinType.BOZ, false);
        JoinRoomRequest joinRoomRequest = new JoinRoomRequest(roomId, bomberInfo);
        var message = new NetworkMessage(MessageType.JOIN_ROOM, client.getGson()
                                                                      .toJson(joinRoomRequest));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; joinRoom ignored (offline mode)");
        }
    }

    public void leaveRoom() {
        var message = new NetworkMessage(MessageType.LEAVE_ROOM, "%s leave room".formatted(bomberName));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Offline can't leave");
        }
        currentRoom = null;
    }

    public void sendChatMessage(String text) {
        if (text == null || text.isBlank() || this.currentRoom == null) {
            return;
        }
        var chatMessageRequest = new ChatMessageRequest(this.currentRoom.getId(), bomberName, text);
        if (client != null && client.isConnected()) {
            var message = new NetworkMessage(MessageType.CHAT_MESSAGE, client.getGson()
                                                                             .toJson(chatMessageRequest));
            client.sendMessage(message);
        }
        else {
            log.debug("Offline");
        }
    }

    public void sendGameAction(Integer keyCode, Boolean isReleased) {
        var bomberActionRequest = new BomberActionRequest(bomberId, keyCode, isReleased);
        var message = new NetworkMessage(MessageType.BOMBER_ACTION, client.getGson()
                                                                          .toJson(bomberActionRequest));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; sendGameAction ignored (offline mode)");
        }
    }

    public void startGame() {
        var message = new NetworkMessage(MessageType.START_GAME, null);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; startGame ignored (offline mode)");
        }
    }

    public void toggleReady(boolean ready) {
        var message = new NetworkMessage(MessageType.UPDATE_READY, client.getGson()
                                                                         .toJson(ready));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
    }

    public void changeSkin(SkinType skinType) {
        var message = new NetworkMessage(MessageType.UPDATE_SKIN, client.getGson()
                                                                        .toJson(skinType));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
    }

    public void changeMap(MapType mapType) {
        var message = new NetworkMessage(MessageType.UPDATE_MAP, client.getGson()
                                                                       .toJson(mapType));
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
    }

    public void requestRoomList() {
        var message = new NetworkMessage(MessageType.ROOM_LIST, null);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; requestRoomList ignored");
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void setBomberId(String bomberId) {
        this.bomberId = bomberId;
        client.setBomberId(bomberId);
    }

    public void keyAction(int keyCode, String bomberId, boolean isReleased) {
        var action = LOOKUP.get(keyCode);
        int index = getIndexByBomberId(action, bomberId);
        if (index != -1) {
            if (isReleased) {
                action.onReleased.accept(bombers.get(index));
            }
            else {
                action.onPressed.accept(bombers.get(index));
            }
        }
    }

    private int getIndexByBomberId(BomberKeyAction action, String bomberId) {
        if (action != null && bombers != null) {
            for (int i = 0; i < bombers.size(); ++i) {
                if (bomberId.equals(bombers.get(i)
                                           .getBomberId())) {
                    return i;
                }
            }
        }

        return -1;
    }
}
