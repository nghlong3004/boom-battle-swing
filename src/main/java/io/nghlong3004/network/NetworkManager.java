package io.nghlong3004.network;

import io.nghlong3004.network.model.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class NetworkManager {

    @Getter
    private static NetworkManager instance;

    private GameWebSocketClient client;
    private String serverUrl;
    @Getter
    private String playerId;
    @Getter
    private String playerName;
    @Getter
    private Lobby currentLobby;
    @Getter
    private List<Lobby> availableLobbies;
    @Getter
    private List<ChatMessage> chatMessages;

    private Consumer<NetworkMessage> messageHandler;

    private NetworkManager() {
        this.availableLobbies = new ArrayList<>();
        this.chatMessages = new ArrayList<>();
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public void connect(String serverUrl, String playerName) {
        this.serverUrl = serverUrl;
        this.playerName = playerName;

        try {
            URI serverUri = new URI(serverUrl);
            client = new GameWebSocketClient(serverUri);
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
        log.info("Received {} lobbies", lobbies.size());
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
                                                    new Object[]{lobbyName, maxPlayers, mapType});
        client.sendMessage(message);
    }

    public void joinLobby(String lobbyId) {
        NetworkMessage message = new NetworkMessage(MessageType.JOIN_LOBBY, lobbyId);
        client.sendMessage(message);
    }

    public void leaveLobby() {
        NetworkMessage message = new NetworkMessage(MessageType.LEAVE_LOBBY, null);
        client.sendMessage(message);
        currentLobby = null;
    }

    public void sendChatMessage(String text) {
        ChatMessage chatMessage = new ChatMessage(playerName, text);
        NetworkMessage message = new NetworkMessage(MessageType.CHAT_MESSAGE, chatMessage);
        client.sendMessage(message);
    }

    public void sendGameAction(GameAction action) {
        NetworkMessage message = new NetworkMessage(MessageType.PLAYER_MOVE, action);
        client.sendMessage(message);
    }

    public void startGame() {
        NetworkMessage message = new NetworkMessage(MessageType.START_GAME, null);
        client.sendMessage(message);
    }

    public void requestLobbyList() {
        NetworkMessage message = new NetworkMessage(MessageType.LOBBY_LIST, null);
        client.sendMessage(message);
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    public void setMessageHandler(Consumer<NetworkMessage> handler) {
        this.messageHandler = handler;
    }

    public void clearChatMessages() {
        chatMessages.clear();
    }
}
