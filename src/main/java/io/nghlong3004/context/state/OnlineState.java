package io.nghlong3004.context.state;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.component.online.LobbyListComponent;
import io.nghlong3004.component.online.LobbyRoomComponent;
import io.nghlong3004.component.online.ServerConnectComponent;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.type.OnlineType;
import lombok.Setter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EnumMap;
import java.util.Map;

public class OnlineState implements GameState {

    private final GameContext context;
    private final Map<OnlineType, GameComponent> componentMap;
    @Setter
    private OnlineType type;

    public OnlineState(GameContext context) {
        this.context = context;
        this.componentMap = new EnumMap<>(OnlineType.class);
        loadComponents();
        this.type = OnlineType.SERVER_CONNECT;
    }

    private void loadComponents() {
        componentMap.put(OnlineType.SERVER_CONNECT, new ServerConnectComponent(context));
        componentMap.put(OnlineType.LOBBY_LIST, new LobbyListComponent(context));
        componentMap.put(OnlineType.LOBBY_ROOM, new LobbyRoomComponent(context));
    }

    @Override
    public void update() {
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
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                } else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    serverConnect.addCharToName(e.getKeyChar());
                }
            }
        } else if (type == OnlineType.LOBBY_LIST) {
            LobbyListComponent lobbyList = (LobbyListComponent) componentMap.get(OnlineType.LOBBY_LIST);
            if (lobbyList.isDialogInputActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    lobbyList.removeCharFromDialog();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                } else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    lobbyList.addCharToDialog(e.getKeyChar());
                }
            }
        } else if (type == OnlineType.LOBBY_ROOM) {
            LobbyRoomComponent lobbyRoom = (LobbyRoomComponent) componentMap.get(OnlineType.LOBBY_ROOM);
            if (lobbyRoom.isChatActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    lobbyRoom.removeCharFromChat();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                } else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
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
    }
}
