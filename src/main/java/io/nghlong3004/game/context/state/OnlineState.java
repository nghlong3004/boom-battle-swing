package io.nghlong3004.game.context.state;

import io.nghlong3004.constant.AudioConstant;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.online.*;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.manager.NetworkManager;
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
    private final NetworkManager networkManager;
    @Getter
    @Setter
    private OnlineType type;

    public OnlineState(GameContext context) {
        this.context = context;
        this.componentMap = new EnumMap<>(OnlineType.class);
        this.networkManager = context.getNetworkManager();
        loadComponents();
        this.type = OnlineType.SERVER_CONNECT;
    }

    private void loadComponents() {
        componentMap.put(OnlineType.SERVER_CONNECT, new ServerConnectComponent(context));
        componentMap.put(OnlineType.ROOM_LIST, new OnlineRoomListComponent(context));
        componentMap.put(OnlineType.ROOM, new OnlineRoomComponent(context));
        componentMap.put(OnlineType.PLAYING, new OnlinePlayComponent(context));
        componentMap.put(OnlineType.OVER, new OnlineOverComponent(context));
        componentMap.put(OnlineType.WIN, new OnlineWinComponent(context));
    }

    public GameComponent getComponent(OnlineType type) {
        return componentMap.get(type);
    }

    @Override
    public void update() {
        networkManager.update();
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
        switch (type) {
            case SERVER_CONNECT -> handleServerConnect(e);
            case ROOM_LIST -> handleRoomList(e);
            case ROOM -> handleRoom(e);
            case PLAYING -> componentMap.get(type)
                                        .keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        componentMap.get(type)
                    .keyReleased(e);
    }

    @Override
    public void on() {
        this.type = OnlineType.SERVER_CONNECT;
        context.getAudio()
               .playSong(AudioConstant.ONLINE);
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
            OnlineRoomComponent lobbyRoom = (OnlineRoomComponent) componentMap.get(OnlineType.ROOM);
            if (lobbyRoom != null) {
                lobbyRoom.handleWheel(amount, p);
            }
        }
    }

    private void handleServerConnect(KeyEvent e) {
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

    private void handleRoomList(KeyEvent e) {
        OnlineRoomListComponent lobbyList = (OnlineRoomListComponent) componentMap.get(OnlineType.ROOM_LIST);
        if (lobbyList.isDialogInputActive()) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                lobbyList.removeCharFromDialog();
            }
            else if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(e.getKeyChar())) {
                lobbyList.addCharToDialog(e.getKeyChar());
            }
        }
    }

    private void handleRoom(KeyEvent e) {
        OnlineRoomComponent lobbyRoom = (OnlineRoomComponent) componentMap.get(OnlineType.ROOM);
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
