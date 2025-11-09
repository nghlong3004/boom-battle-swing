package io.nghlong3004.component.online;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.OnlineState;
import io.nghlong3004.network.NetworkManager;
import io.nghlong3004.network.model.Lobby;
import io.nghlong3004.network.model.PlayerInfo;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.OnlineType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;

public class LobbyListComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final CreateLobbyDialog createLobbyDialog;
    private List<Rectangle> lobbyButtons;
    private Rectangle createButton;
    private Rectangle refreshButton;
    private Rectangle backButton;
    private int hoveredLobbyIndex = -1;

    public LobbyListComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();
        this.createLobbyDialog = new CreateLobbyDialog();
        this.lobbyButtons = new ArrayList<>();

        int buttonWidth = (int) (200 * SCALE);
        int buttonHeight = (int) (50 * SCALE);
        int centerX = GAME_WIDTH / 2;

        createButton = new Rectangle(centerX - buttonWidth - 20, GAME_HEIGHT - 100, buttonWidth, buttonHeight);
        refreshButton = new Rectangle(centerX + 20, GAME_HEIGHT - 100, buttonWidth, buttonHeight);
        backButton = new Rectangle(50, GAME_HEIGHT - 100, (int) (150 * SCALE), buttonHeight);
    }

    @Override
    public void update() {
        networkManager.update();
        updateLobbyButtons();
    }

    private void updateLobbyButtons() {
        lobbyButtons.clear();
        List<Lobby> lobbies = networkManager.getAvailableLobbies();

        int startY = 150;
        int spacing = 80;
        int buttonWidth = GAME_WIDTH - 200;
        int buttonHeight = 60;

        for (int i = 0; i < lobbies.size(); i++) {
            Rectangle button = new Rectangle(100, startY + i * spacing, buttonWidth, buttonHeight);
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

        renderButton(g2d, createButton, "Create Lobby", new Color(46, 204, 113));
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

                Lobby currentLobby = new Lobby(lobby.getLobbyId(), lobby.getLobbyName(), "you", lobby.getMaxPlayers());
                currentLobby.setMapType(lobby.getMapType());
                currentLobby.getPlayers()
                            .addAll(lobby.getPlayers());
                currentLobby.getPlayers()
                            .add(new PlayerInfo("you", "You", "boz", false, false));

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
        
        Lobby newLobby = new Lobby("lobby_" + System.currentTimeMillis(), lobbyName, "you", maxPlayers);
        newLobby.setMapType(mapType);
        newLobby.getPlayers().add(new PlayerInfo("you", "You", skinType, true, true));
        
        networkManager.getAvailableLobbies().add(newLobby);
        
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
