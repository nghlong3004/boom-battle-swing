package io.nghlong3004.game.context.state;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.online.RoomComponent;
import io.nghlong3004.game.component.online.RoomListComponent;
import io.nghlong3004.game.component.online.ServerConnectComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.BomberInfo;
import io.nghlong3004.model.Room;
import io.nghlong3004.model.type.OnlineType;
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
    }

    private BomberInfo getLocal(NetworkManager nm, Room lobby) {
        String playerId = nm.getBomberId();
        String playerName = nm.getBomberName();
        for (var bomberInfo : lobby.getBomberInfos()) {
            if ((playerId != null && playerId.equals(bomberInfo.getId())) || (playerName != null && playerName.equals(
                    bomberInfo.getName())) || "you".equalsIgnoreCase(bomberInfo.getId()) || "you".equalsIgnoreCase(
                    bomberInfo.getName())) {
                return bomberInfo;
            }
        }
        return null;
    }

    private void loadComponents() {
        componentMap.put(OnlineType.SERVER_CONNECT, new ServerConnectComponent(context));
        componentMap.put(OnlineType.ROOM_LIST, new RoomListComponent(context));
        componentMap.put(OnlineType.ROOM, new RoomComponent(context));
    }

    @Override
    public void update() {
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
        else if (type == OnlineType.ROOM_LIST) {
            RoomListComponent lobbyList = (RoomListComponent) componentMap.get(OnlineType.ROOM_LIST);
            if (lobbyList.isDialogInputActive()) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    lobbyList.removeCharFromDialog();
                }
                else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                    lobbyList.addCharToDialog(e.getKeyChar());
                }
            }
        }
        else if (type == OnlineType.ROOM) {
            RoomComponent lobbyRoom = (RoomComponent) componentMap.get(OnlineType.ROOM);
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
        if (type == OnlineType.ROOM) {
            RoomComponent lobbyRoom = (RoomComponent) componentMap.get(OnlineType.ROOM);
            if (lobbyRoom != null) {
                lobbyRoom.handleWheel(amount, p);
            }
        }
    }
}
