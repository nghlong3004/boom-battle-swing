package io.nghlong3004.model.type;

public enum MessageType {
    CONNECT,
    DISCONNECT,
    CREATE_LOBBY,
    JOIN_LOBBY,
    LEAVE_LOBBY,
    START_GAME,
    CHAT_MESSAGE,
    PLAYER_MOVE,
    PLACE_BOMB,
    GAME_STATE_UPDATE,
    LOBBY_LIST,
    LOBBY_UPDATE,
    UPDATE_READY,
    UPDATE_SKIN,
    UPDATE_MAP,
    ERROR
}
