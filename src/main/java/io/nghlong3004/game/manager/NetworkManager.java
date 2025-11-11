package io.nghlong3004.game.manager;

import io.nghlong3004.model.type.MessageType;
import io.nghlong3004.websocket.WebSocketClient;
import io.nghlong3004.websocket.model.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class NetworkManager {

    @Getter
    private static final NetworkManager instance = new NetworkManager();

    private WebSocketClient client;
    private String serverUrl;
    @Getter
    private String playerId;
    @Getter
    private String playerName;
    @Setter
    @Getter
    private Lobby currentLobby;
    @Getter
    private List<Lobby> availableLobbies;
    @Getter
    private final List<ChatMessage> chatMessages;

    @Setter
    private Consumer<NetworkMessage> messageHandler;

    private NetworkManager() {
        this.availableLobbies = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
    }

    public void connect(String serverUrl, String playerName) {
        this.serverUrl = serverUrl;
        this.playerName = playerName;

        try {
            URI serverUri = new URI(serverUrl);
            client = new WebSocketClient(serverUri);
            client.connectBlocking();
            log.info("Connected to server: {}", serverUrl);
        } catch (Exception e) {
            log.error("Failed to connect to server", e);
        }
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
            handleMessage(message);
        }
    }

    private void handleMessage(NetworkMessage message) {
        log.debug("Handling message: {}", message.getType());

        switch (message.getType()) {
            case CONNECT:
                handleConnect(message);
                break;
            case LOBBY_LIST:
                handleLobbyList(message);
                break;
            case LOBBY_UPDATE:
                handleLobbyUpdate(message);
                break;
            case CHAT_MESSAGE:
                handleChatMessage(message);
                break;
            case START_GAME:
                handleStartGame(message);
                break;
            case GAME_STATE_UPDATE:
                handleGameStateUpdate(message);
                break;
            case ERROR:
                handleError(message);
                break;
            default:
                log.warn("Unhandled message type: {}", message.getType());
        }

        if (messageHandler != null) {
            messageHandler.accept(message);
        }
    }

    private void handleConnect(NetworkMessage message) {
        this.playerId = (String) message.getData();
        client.setPlayerId(playerId);
        log.info("Player ID assigned: {}", playerId);
    }

    private void handleLobbyList(NetworkMessage message) {
        List<Lobby> lobbies = (List<Lobby>) message.getData();
        this.availableLobbies = lobbies;
        if (this.availableLobbies == null) {
            this.availableLobbies = new ArrayList<>();
        }
        log.info("Received {} lobbies", this.availableLobbies.size());
    }

    private void handleLobbyUpdate(NetworkMessage message) {
        Lobby lobby = (Lobby) message.getData();
        this.currentLobby = lobby;
        log.info("Lobby updated: {}", lobby.getLobbyName());
    }

    private void handleChatMessage(NetworkMessage message) {
        ChatMessage chatMessage = (ChatMessage) message.getData();
        chatMessages.add(chatMessage);
        log.debug("Chat message from {}: {}", chatMessage.getPlayerName(), chatMessage.getMessage());
    }

    private void handleStartGame(NetworkMessage message) {
        log.info("Game starting!");
    }

    private void handleGameStateUpdate(NetworkMessage message) {
        log.debug("Game state updated");
    }

    private void handleError(NetworkMessage message) {
        log.error("Server error: {}", message.getData());
    }

    public void createLobby(String lobbyName, int maxPlayers, String mapType) {
        NetworkMessage message = new NetworkMessage(MessageType.CREATE_LOBBY,
                                                    new Object[]{lobbyName, maxPlayers, mapType, playerId});
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; createLobby ignored (offline mode)");
        }
    }

    public void joinLobby(String lobbyId) {
        NetworkMessage message = new NetworkMessage(MessageType.JOIN_LOBBY, lobbyId);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; joinLobby ignored (offline mode)");
        }
    }

    public void leaveLobby() {
        NetworkMessage message = new NetworkMessage(MessageType.LEAVE_LOBBY, null);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; leaving lobby locally");
            if (currentLobby != null) {
                String lobbyId = currentLobby.getLobbyId();
                Lobby listLobby = null;
                for (Lobby l : availableLobbies) {
                    if (l.getLobbyId() != null && l.getLobbyId()
                                                   .equals(lobbyId)) {
                        listLobby = l;
                        break;
                    }
                }
                if (listLobby != null) {
                    PlayerInfo leaving = null;
                    for (PlayerInfo p : listLobby.getPlayers()) {
                        if ((playerId != null && playerId.equals(
                                p.getPlayerId())) || (playerName != null && playerName.equals(
                                p.getPlayerName())) || "you".equalsIgnoreCase(
                                p.getPlayerId()) || "you".equalsIgnoreCase(p.getPlayerName())) {
                            leaving = p;
                            break;
                        }
                    }
                    if (leaving != null) {
                        boolean wasHost = leaving.isHost() || (listLobby.getHostId() != null && listLobby.getHostId()
                                                                                                         .equals(leaving.getPlayerId()));
                        listLobby.getPlayers()
                                 .remove(leaving);
                        if (listLobby.getPlayers()
                                     .isEmpty()) {
                            availableLobbies.remove(listLobby);
                        }
                        else if (wasHost) {
                            PlayerInfo newHost = listLobby.getPlayers()
                                                          .getFirst();
                            listLobby.setHostId(newHost.getPlayerId());
                            for (PlayerInfo p : listLobby.getPlayers()) {
                                p.setHost(p == newHost);
                            }
                        }
                    }
                }
            }
        }
        currentLobby = null;
    }

    public void sendChatMessage(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        String name = (playerName == null || playerName.isBlank()) ? "You" : playerName;
        ChatMessage chatMessage = new ChatMessage(name, text);
        if (client != null && client.isConnected()) {
            NetworkMessage message = new NetworkMessage(MessageType.CHAT_MESSAGE, chatMessage);
            client.sendMessage(message);
        }
        else {
            chatMessages.add(chatMessage);
            log.debug("Offline chat message: {}: {}", name, text);
        }
    }

    public void sendGameAction(GameAction action) {
        NetworkMessage message = new NetworkMessage(MessageType.PLAYER_MOVE, action);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; sendGameAction ignored (offline mode)");
        }
    }

    public void startGame() {
        NetworkMessage message = new NetworkMessage(MessageType.START_GAME, null);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; startGame ignored (offline mode)");
        }
    }

    public void toggleReady(boolean ready) {
        NetworkMessage message = new NetworkMessage(MessageType.UPDATE_READY, ready);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            if (currentLobby != null) {
                for (PlayerInfo p : currentLobby.getPlayers()) {
                    if ((playerId != null && playerId.equals(
                            p.getPlayerId())) || (playerName != null && playerName.equals(
                            p.getPlayerName())) || "you".equalsIgnoreCase(p.getPlayerId()) || "You".equalsIgnoreCase(
                            p.getPlayerName())) {
                        p.setReady(ready);
                        break;
                    }
                }
            }
        }
    }

    public void changeSkin(String skinKey) {
        NetworkMessage message = new NetworkMessage(MessageType.UPDATE_SKIN, skinKey);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            if (currentLobby != null) {
                for (PlayerInfo p : currentLobby.getPlayers()) {
                    if ((playerId != null && playerId.equals(
                            p.getPlayerId())) || (playerName != null && playerName.equals(
                            p.getPlayerName())) || "you".equalsIgnoreCase(p.getPlayerId()) || "You".equalsIgnoreCase(
                            p.getPlayerName())) {
                        p.setSkinType(skinKey);
                        break;
                    }
                }
            }
        }
    }

    public void changeMap(String mapKey) {
        NetworkMessage message = new NetworkMessage(MessageType.UPDATE_MAP, mapKey);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            if (currentLobby != null) {
                currentLobby.setMapType(mapKey);
            }
        }
    }

    public void requestLobbyList() {
        NetworkMessage message = new NetworkMessage(MessageType.LOBBY_LIST, null);
        if (client != null && client.isConnected()) {
            client.sendMessage(message);
        }
        else {
            log.debug("Client not connected; requestLobbyList ignored (offline mode)");
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void clearChatMessages() {
        chatMessages.clear();
    }
}
