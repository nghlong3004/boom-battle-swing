package io.nghlong3004.game.context.state;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.online.LobbyListComponent;
import io.nghlong3004.game.component.online.LobbyRoomComponent;
import io.nghlong3004.game.component.online.ServerConnectComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.type.*;
import io.nghlong3004.websocket.model.Lobby;
import io.nghlong3004.websocket.model.PlayerInfo;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class OnlineState implements GameState {

    private final GameContext context;
    private final Map<OnlineType, GameComponent> componentMap;
    @Getter
    @Setter
    private OnlineType type;

    public OnlineState(GameContext context) {
        this.context = context;
        this.componentMap = new EnumMap<>(OnlineType.class);
        loadComponents();
        this.type = OnlineType.SERVER_CONNECT;
        NetworkManager.getInstance()
                      .setMessageHandler(msg -> {
                          if (msg.getType() == MessageType.START_GAME) {
                              LobbyRoomComponent lobbyRoom = (LobbyRoomComponent) componentMap.get(
                                      OnlineType.LOBBY_ROOM);
                              NetworkManager nm = NetworkManager.getInstance();
                              Lobby lobby = nm.getCurrentLobby();
                              if (lobby != null) {
                                  MapType selectedMap = MapType.DESERT_MODE;
                                  for (MapType mt : MapType.values()) {
                                      if (mt.getAssetKey()
                                            .equalsIgnoreCase(lobby.getMapType())) {
                                          selectedMap = mt;
                                          break;
                                      }
                                  }
                                  context.setMapType(selectedMap);
                                  context.setGameType(GameType.ONLINE);
                                  context.setPlayerCount(PlayerCountType.ONE_PLAYER);
                                  context.setNumberBomber(1);
                                  SkinType[] skinsArr = context.getSkinType();
                                  if (skinsArr == null || skinsArr.length < 2) {
                                      skinsArr = new SkinType[2];
                                  }
                                  PlayerInfo me = lobbyRoom != null ? getLocal(nm, lobby) : null;
                                  SkinType mySkin = SkinType.BOZ;
                                  if (me != null) {
                                      for (SkinType s : SkinType.values()) {
                                          if (s.getAssetKey()
                                               .equalsIgnoreCase(me.getSkinType())) {
                                              mySkin = s;
                                              break;
                                          }
                                      }
                                  }
                                  skinsArr[0] = mySkin;
                                  context.setSkinType(skinsArr);
                                  context.changeState(GameStateType.PLAYING);
                              }
                          }
                      });
    }

    private PlayerInfo getLocal(NetworkManager nm, Lobby lobby) {
        String playerId = nm.getPlayerId();
        String playerName = nm.getPlayerName();
        for (PlayerInfo p : lobby.getPlayers()) {
            if ((playerId != null && playerId.equals(p.getPlayerId())) || (playerName != null && playerName.equals(
                    p.getPlayerName())) || "you".equalsIgnoreCase(p.getPlayerId()) || "you".equalsIgnoreCase(
                    p.getPlayerName())) {
                return p;
            }
        }
        return null;
    }

    private void loadComponents() {
        componentMap.put(OnlineType.SERVER_CONNECT, new ServerConnectComponent(context));
        componentMap.put(OnlineType.LOBBY_LIST, new LobbyListComponent(context));
        componentMap.put(OnlineType.LOBBY_ROOM, new LobbyRoomComponent(context));
    }

    @Override
    public void update() {
        // Centralize network message pumping here so components stay UI-focused
        NetworkManager.getInstance()
                      .update();
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.update();
        }
    }

    @Override
    public void render(Graphics g) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.render(g);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.mouseDragged(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameComponent component = componentMap.get(type);
        if (component != null) {
            component.mouseClicked(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (type == OnlineType.SERVER_CONNECT) {
            ServerConnectComponent serverConnect = (ServerConnectComponent) componentMap.get(OnlineType.SERVER_CONNECT);
            if (serverConnect.isAnyInputActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    serverConnect.removeCharFromName();
                }
                else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    serverConnect.addCharToName(e.getKeyChar());
                }
            }
        }
        else if (type == OnlineType.LOBBY_LIST) {
            LobbyListComponent lobbyList = (LobbyListComponent) componentMap.get(OnlineType.LOBBY_LIST);
            if (lobbyList.isDialogInputActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    lobbyList.removeCharFromDialog();
                }
                else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    lobbyList.addCharToDialog(e.getKeyChar());
                }
            }
        }
        else if (type == OnlineType.LOBBY_ROOM) {
            LobbyRoomComponent lobbyRoom = (LobbyRoomComponent) componentMap.get(OnlineType.LOBBY_ROOM);
            if (lobbyRoom.isChatActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    lobbyRoom.removeCharFromChat();
                }
                else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    lobbyRoom.addCharToChat(e.getKeyChar());
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void on() {
        this.type = OnlineType.SERVER_CONNECT;
    }

    @Override
    public void off() {
        GameComponent comp = componentMap.get(OnlineType.SERVER_CONNECT);
        if (comp instanceof ServerConnectComponent scc) {
            scc.cancelConnecting();
        }
    }

    public void handleMouseWheel(int amount, Point p) {
        if (type == OnlineType.LOBBY_ROOM) {
            LobbyRoomComponent lobbyRoom = (LobbyRoomComponent) componentMap.get(OnlineType.LOBBY_ROOM);
            if (lobbyRoom != null) {
                lobbyRoom.handleWheel(amount, p);
            }
        }
    }
}
