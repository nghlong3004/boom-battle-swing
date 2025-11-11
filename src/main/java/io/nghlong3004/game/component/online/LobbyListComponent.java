package io.nghlong3004.game.component.online;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OnlineType;
import io.nghlong3004.websocket.model.Lobby;
import io.nghlong3004.websocket.model.PlayerInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;

public class LobbyListComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final CreateLobbyDialog createLobbyDialog;
    private final List<Rectangle> lobbyButtons;
    private final Rectangle createButton;
    private final Rectangle refreshButton;
    private final Rectangle backButton;
    private int hoveredLobbyIndex = -1;
    private static final boolean OFFLINE_TEST_MODE = false;
    private static final int OFFLINE_MOCK_PLAYERS = 3;

    public LobbyListComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();
        this.createLobbyDialog = new CreateLobbyDialog();
        this.lobbyButtons = new ArrayList<>();

        int buttonWidth = (int) (140 * SCALE);
        int buttonHeight = (int) (50 * SCALE);

        int gap = (int) (20 * SCALE);
        int totalWidth = buttonWidth * 3 + gap * 2;
        int startX = (GAME_WIDTH - totalWidth) / 2;
        int y = GAME_HEIGHT - (int) (80 * SCALE);
        backButton = new Rectangle(startX, y, buttonWidth, buttonHeight);
        createButton = new Rectangle(startX + buttonWidth + gap, y, buttonWidth, buttonHeight);
        refreshButton = new Rectangle(startX + (buttonWidth + gap) * 2, y, buttonWidth, buttonHeight);
    }

    @Override
    public void update() {
        networkManager.update();
        updateLobbyButtons();
    }

    private void updateLobbyButtons() {
        lobbyButtons.clear();
        List<Lobby> lobbies = networkManager.getAvailableLobbies();

        int listMarginX = (int) (80 * SCALE);
        int listTop = (int) (140 * SCALE);
        int listBottom = (int) (GAME_HEIGHT - 150 * SCALE);
        int areaHeight = Math.max(0, listBottom - listTop);

        int itemHeight = (int) (60 * SCALE);
        int itemSpacing = (int) (20 * SCALE);
        int listWidth = GAME_WIDTH - listMarginX * 2;
        int listX = (GAME_WIDTH - listWidth) / 2;

        int n = lobbies.size();
        int totalHeight = n > 0 ? n * itemHeight + (n - 1) * itemSpacing : 0;
        int startY = listTop + Math.max(0, (areaHeight - totalHeight) / 2);

        for (int i = 0; i < n; i++) {
            Rectangle button = new Rectangle(listX, startY + i * (itemHeight + itemSpacing), listWidth, itemHeight);
            lobbyButtons.add(button);
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(new Color(20, 25, 35));
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (48 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        String title = "Game Lobbies";
        int titleWidth = g2d.getFontMetrics()
                            .stringWidth(title);
        g2d.drawString(title, GAME_WIDTH / 2 - titleWidth / 2, 80);

        List<Lobby> lobbies = networkManager.getAvailableLobbies();
        for (int i = 0; i < lobbies.size() && i < lobbyButtons.size(); i++) {
            renderLobbyButton(g2d, lobbyButtons.get(i), lobbies.get(i), i == hoveredLobbyIndex);
        }

        if (lobbies.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.ITALIC, (int) (28 * SCALE)));
            g2d.setColor(Color.GRAY);
            String noLobbies = "No lobbies available. Create one to start!";
            int width = g2d.getFontMetrics()
                           .stringWidth(noLobbies);
            g2d.drawString(noLobbies, GAME_WIDTH / 2 - width / 2, GAME_HEIGHT / 2);
        }

        renderButton(g2d, createButton, "Create", new Color(46, 204, 113));
        renderButton(g2d, refreshButton, "Refresh", new Color(52, 152, 219));
        renderButton(g2d, backButton, "Back", new Color(231, 76, 60));

        createLobbyDialog.render(g2d);
    }

    private void renderLobbyButton(Graphics2D g2d, Rectangle rect, Lobby lobby, boolean hovered) {
        Color bgColor = hovered ? new Color(45, 52, 65) : new Color(30, 35, 45);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        Color borderColor = hovered ? new Color(100, 200, 255) : new Color(60, 70, 85);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (28 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString(lobby.getLobbyName(), rect.x + 20, rect.y + 35);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (20 * SCALE)));
        g2d.setColor(new Color(180, 180, 200));
        String info = String.format("Players: %d/%d | Map: %s", lobby.getPlayers()
                                                                     .size(), lobby.getMaxPlayers(),
                                    lobby.getMapType());
        g2d.drawString(info, rect.x + 20, rect.y + 60);
    }

    private void renderButton(Graphics2D g2d, Rectangle button, String text, Color color) {
        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 12, 12);

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(button.x, button.y, button.width, button.height, 12, 12);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (24 * SCALE)));
        g2d.setColor(Color.WHITE);
        int textWidth = g2d.getFontMetrics()
                           .stringWidth(text);
        g2d.drawString(text, button.x + button.width / 2 - textWidth / 2, button.y + button.height / 2 + 8);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (createLobbyDialog.isVisible()) {
            if (createLobbyDialog.isCreateClicked(e)) {
                createNewLobby();
            }
            createLobbyDialog.handleMousePressed(e);
            return;
        }

        if (createButton.contains(e.getPoint())) {
            createLobbyDialog.show();
            return;
        }

        if (refreshButton.contains(e.getPoint())) {
            networkManager.requestLobbyList();
        }

        if (backButton.contains(e.getPoint())) {
            networkManager.disconnect();
            context.changeState(GameStateType.MENU);
        }

        List<Lobby> lobbies = networkManager.getAvailableLobbies();
        for (int i = 0; i < lobbyButtons.size() && i < lobbies.size(); i++) {
            if (lobbyButtons.get(i)
                            .contains(e.getPoint())) {
                Lobby lobby = lobbies.get(i);

                if (networkManager.isConnected()) {
                    networkManager.joinLobby(lobby.getLobbyId());
                }
                else {
                    Lobby currentLobby = new Lobby(lobby.getLobbyId(), lobby.getLobbyName(), lobby.getHostId(),
                                                   lobby.getMaxPlayers());
                    currentLobby.setMapType(lobby.getMapType());
                    currentLobby.getPlayers()
                                .addAll(lobby.getPlayers());
                    currentLobby.getPlayers()
                                .add(new PlayerInfo("you", "You", "boz", false, false));
                    networkManager.setCurrentLobby(currentLobby);

                    boolean exists = false;
                    for (PlayerInfo p : lobby.getPlayers()) {
                        if ("you".equalsIgnoreCase(p.getPlayerId()) || "you".equalsIgnoreCase(p.getPlayerName())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists && lobby.getPlayers()
                                        .size() < lobby.getMaxPlayers()) {
                        lobby.getPlayers()
                             .add(new PlayerInfo("you", "You", "boz", false, false));
                    }
                }

                OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
                onlineState.setType(OnlineType.LOBBY_ROOM);
                break;
            }
        }
    }

    private void createNewLobby() {
        String lobbyName = createLobbyDialog.getLobbyName();
        String mapType = createLobbyDialog.getSelectedMap();
        String skinType = createLobbyDialog.getSelectedSkin();
        int maxPlayers = 4;

        if (networkManager.isConnected()) {
            networkManager.createLobby(lobbyName, maxPlayers, mapType);
        }
        else {
            Lobby newLobby = new Lobby("lobby_" + System.currentTimeMillis(), lobbyName, "you", maxPlayers);
            newLobby.setMapType(mapType);
            newLobby.getPlayers()
                    .add(new PlayerInfo("you", "You", skinType, true, true));

            networkManager.getAvailableLobbies()
                          .add(newLobby);
            networkManager.setCurrentLobby(newLobby);
        }

        createLobbyDialog.hide();

        OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
        onlineState.setType(OnlineType.LOBBY_ROOM);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (createLobbyDialog.isVisible()) {
            createLobbyDialog.handleMouseMoved(e);
            return;
        }

        hoveredLobbyIndex = -1;
        List<Lobby> lobbies = networkManager.getAvailableLobbies();
        for (int i = 0; i < lobbyButtons.size() && i < lobbies.size(); i++) {
            if (lobbyButtons.get(i)
                            .contains(e.getPoint())) {
                hoveredLobbyIndex = i;
                break;
            }
        }
    }

    public boolean isDialogInputActive() {
        return createLobbyDialog.isVisible() && createLobbyDialog.isNameInputActive();
    }

    public void addCharToDialog(char c) {
        createLobbyDialog.addCharToName(c);
    }

    public void removeCharFromDialog() {
        createLobbyDialog.removeCharFromName();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
