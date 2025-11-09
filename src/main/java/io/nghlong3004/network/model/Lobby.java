package io.nghlong3004.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private String lobbyId;
    private String lobbyName;
    private String hostId;
    private List<PlayerInfo> players;
    private int maxPlayers;
    private String mapType;
    private boolean isStarted;

    public Lobby(String lobbyId, String lobbyName, String hostId, int maxPlayers) {
        this.lobbyId = lobbyId;
        this.lobbyName = lobbyName;
        this.hostId = hostId;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.isStarted = false;
    }
}
