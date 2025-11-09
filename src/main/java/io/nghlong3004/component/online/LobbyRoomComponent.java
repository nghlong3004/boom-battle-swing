package io.nghlong3004.component.online;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.OnlineState;
import io.nghlong3004.network.NetworkManager;
import io.nghlong3004.network.model.ChatMessage;
import io.nghlong3004.network.model.Lobby;
import io.nghlong3004.network.model.PlayerInfo;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.OnlineType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;

public class LobbyRoomComponent extends GameComponent {

    private final NetworkManager networkManager;
    private Rectangle startButton;
    private Rectangle readyButton;
    private Rectangle leaveButton;
    private Rectangle chatInputBox;
    private Rectangle sendButton;
    private Rectangle chatScrollArea;
    private Rectangle playerScrollArea;
    private String chatInput = "";
    private boolean chatInputActive = false;
    private int chatScrollOffset = 0;
    private int playerScrollOffset = 0;
    private int maxChatScroll = 0;
    private int maxPlayerScroll = 0;
    private Point lastDragPoint = null;
    private boolean isDraggingChat = false;
    private boolean isDraggingPlayer = false;

    public LobbyRoomComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();

        int buttonWidth = (int) (180 * SCALE);
        int buttonHeight = (int) (55 * SCALE);

        readyButton = new Rectangle(GAME_WIDTH - 420, GAME_HEIGHT - 100, buttonWidth, buttonHeight);
        startButton = new Rectangle(GAME_WIDTH - 220, GAME_HEIGHT - 100, buttonWidth, buttonHeight);
        leaveButton = new Rectangle(50, GAME_HEIGHT - 100, (int) (150 * SCALE), buttonHeight);

        int chatInputWidth = (int) (450 * SCALE);
        int chatInputHeight = (int) (50 * SCALE);
        chatInputBox = new Rectangle(50, GAME_HEIGHT - 190, chatInputWidth, chatInputHeight);
        sendButton = new Rectangle(50 + chatInputWidth + 10, GAME_HEIGHT - 190, (int) (120 * SCALE), chatInputHeight);

        int playerPanelX = 50;
        int playerPanelY = 130;
        int playerPanelWidth = (int) (380 * SCALE);
        int playerPanelHeight = (int) (420 * SCALE);
        playerScrollArea = new Rectangle(playerPanelX, playerPanelY, playerPanelWidth, playerPanelHeight);

        int chatPanelX = playerPanelX + playerPanelWidth + 30;
        int chatPanelY = 130;
        int chatPanelWidth = GAME_WIDTH - chatPanelX - 50;
        int chatPanelHeight = (int) (420 * SCALE);
        chatScrollArea = new Rectangle(chatPanelX, chatPanelY, chatPanelWidth, chatPanelHeight);
    }

    @Override
    public void update() {
        networkManager.update();
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(new Color(20, 25, 35));
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        Lobby lobby = networkManager.getCurrentLobby();
        if (lobby == null) {
            renderNoLobby(g2d);
            return;
        }

        renderLobbyInfo(g2d, lobby);
        renderPlayerList(g2d, lobby);
        renderChatArea(g2d);
        renderChatInput(g2d);

        renderButton(g2d, readyButton, "Ready", new Color(46, 204, 113), false);
        renderButton(g2d, startButton, "Start", new Color(52, 152, 219), false);
        renderButton(g2d, leaveButton, "Leave", new Color(231, 76, 60), false);
    }

    private void renderNoLobby(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (32 * SCALE)));
        g2d.setColor(Color.RED);
        String msg = "Not in a lobby";
        int width = g2d.getFontMetrics()
                       .stringWidth(msg);
        g2d.drawString(msg, GAME_WIDTH / 2 - width / 2, GAME_HEIGHT / 2);
    }

    private void renderLobbyInfo(Graphics2D g2d, Lobby lobby) {
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (36 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString(lobby.getLobbyName(), 50, 70);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (22 * SCALE)));
        g2d.setColor(new Color(180, 190, 200));
        String info = String.format("Map: %s  •  Players: %d/%d", lobby.getMapType(), lobby.getPlayers()
                                                                                           .size(),
                                    lobby.getMaxPlayers());
        g2d.drawString(info, 50, 105);
    }

    private void renderPlayerList(Graphics2D g2d, Lobby lobby) {
        int panelX = playerScrollArea.x;
        int panelY = playerScrollArea.y;
        int panelWidth = playerScrollArea.width;
        int panelHeight = playerScrollArea.height;

        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("Players", panelX + 20, panelY + 40);

        Shape oldClip = g2d.getClip();
        g2d.setClip(panelX + 10, panelY + 55, panelWidth - 20, panelHeight - 65);

        List<PlayerInfo> players = lobby.getPlayers();
        int itemHeight = 75;
        int contentHeight = players.size() * itemHeight;
        maxPlayerScroll = Math.max(0, contentHeight - (panelHeight - 65));
        playerScrollOffset = Math.max(0, Math.min(playerScrollOffset, maxPlayerScroll));

        int yOffset = 70 - playerScrollOffset;
        for (int i = 0; i < players.size(); i++) {
            PlayerInfo player = players.get(i);
            int y = panelY + yOffset + i * itemHeight;

            if (y + itemHeight < panelY + 55 || y > panelY + panelHeight) {
                continue;
            }

            g2d.setColor(new Color(45, 52, 65, 180));
            g2d.fillRoundRect(panelX + 15, y, panelWidth - 30, itemHeight - 10, 10, 10);

            g2d.setFont(new Font("Arial", Font.BOLD, (int) (22 * SCALE)));
            g2d.setColor(player.isHost() ? new Color(255, 215, 0) : Color.WHITE);
            String name = player.getPlayerName();
            if (player.isHost()) {
                name += " 👑";
            }
            g2d.drawString(name, panelX + 25, y + 28);

            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (18 * SCALE)));
            g2d.setColor(new Color(180, 180, 200));
            g2d.drawString("Skin: " + player.getSkinType(), panelX + 25, y + 50);

            g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * SCALE)));
            if (player.isReady()) {
                g2d.setColor(new Color(46, 204, 113));
                g2d.drawString("✓ Ready", panelX + panelWidth - 100, y + 40);
            }
            else {
                g2d.setColor(new Color(231, 76, 60));
                g2d.drawString("Not Ready", panelX + panelWidth - 110, y + 40);
            }
        }

        g2d.setClip(oldClip);

        if (maxPlayerScroll > 0) {
            renderScrollbar(g2d, panelX + panelWidth - 10, panelY + 55, 8, panelHeight - 65, playerScrollOffset,
                            maxPlayerScroll);
        }
    }

    private void renderChatArea(Graphics2D g2d) {
        int chatX = chatScrollArea.x;
        int chatY = chatScrollArea.y;
        int chatWidth = chatScrollArea.width;
        int chatHeight = chatScrollArea.height;

        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(chatX, chatY, chatWidth, chatHeight, 15, 15);

        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(chatX, chatY, chatWidth, chatHeight, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("Chat", chatX + 20, chatY + 40);

        Shape oldClip = g2d.getClip();
        g2d.setClip(chatX + 10, chatY + 55, chatWidth - 20, chatHeight - 65);

        List<ChatMessage> messages = networkManager.getChatMessages();
        int lineHeight = 28;
        int contentHeight = messages.size() * lineHeight;
        maxChatScroll = Math.max(0, contentHeight - (chatHeight - 65));
        chatScrollOffset = Math.max(0, Math.min(chatScrollOffset, maxChatScroll));

        int messageY = chatY + 75 - chatScrollOffset;
        for (int i = 0; i < messages.size(); i++) {
            ChatMessage msg = messages.get(i);
            int y = messageY + i * lineHeight;

            if (y + lineHeight < chatY + 55 || y > chatY + chatHeight) {
                continue;
            }

            g2d.setFont(new Font("Arial", Font.BOLD, (int) (18 * SCALE)));
            String sender = msg.getPlayerName();
            if (sender.equals("System")) {
                g2d.setColor(new Color(100, 200, 255));
            }
            else if (sender.equals("You")) {
                g2d.setColor(new Color(46, 204, 113));
            }
            else {
                g2d.setColor(new Color(255, 215, 0));
            }
            g2d.drawString(sender + ":", chatX + 20, y);

            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (18 * SCALE)));
            g2d.setColor(Color.WHITE);
            int nameWidth = g2d.getFontMetrics()
                               .stringWidth(sender + ": ");
            g2d.drawString(msg.getMessage(), chatX + 20 + nameWidth, y);
        }

        g2d.setClip(oldClip);

        if (maxChatScroll > 0) {
            renderScrollbar(g2d, chatX + chatWidth - 10, chatY + 55, 8, chatHeight - 65, chatScrollOffset,
                            maxChatScroll);
        }
    }

    private void renderScrollbar(Graphics2D g2d, int x, int y, int width, int height, int offset, int maxOffset) {
        g2d.setColor(new Color(60, 70, 85, 150));
        g2d.fillRoundRect(x, y, width, height, width, width);

        if (maxOffset > 0) {
            int scrollbarHeight = Math.max(30, (int) (height * ((float) height / (height + maxOffset))));
            int scrollbarY = y + (int) ((height - scrollbarHeight) * ((float) offset / maxOffset));

            g2d.setColor(new Color(100, 200, 255));
            g2d.fillRoundRect(x, scrollbarY, width, scrollbarHeight, width, width);
        }
    }

    private void renderChatInput(Graphics2D g2d) {
        g2d.setColor(chatInputActive ? new Color(45, 52, 65) : new Color(35, 40, 50));
        g2d.fillRoundRect(chatInputBox.x, chatInputBox.y, chatInputBox.width, chatInputBox.height, 12, 12);

        g2d.setColor(chatInputActive ? new Color(100, 200, 255) : new Color(80, 90, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(chatInputBox.x, chatInputBox.y, chatInputBox.width, chatInputBox.height, 12, 12);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (22 * SCALE)));
        String displayText = chatInput.isEmpty() ? "Type a message..." : chatInput;
        g2d.setColor(chatInput.isEmpty() ? Color.GRAY : Color.WHITE);
        g2d.drawString(displayText + (chatInputActive && !chatInput.isEmpty() ? "|" : ""), chatInputBox.x + 15,
                       chatInputBox.y + 33);

        renderButton(g2d, sendButton, "Send", new Color(52, 152, 219), chatInput.isEmpty());
    }

    private void renderButton(Graphics2D g2d, Rectangle button, String text, Color color, boolean disabled) {
        Color bgColor = disabled ? color.darker()
                                        .darker() : color.darker();
        g2d.setColor(bgColor);
        g2d.fillRoundRect(button.x, button.y, button.width, button.height, 12, 12);

        g2d.setColor(disabled ? color.darker() : color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(button.x, button.y, button.width, button.height, 12, 12);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (24 * SCALE)));
        g2d.setColor(disabled ? Color.GRAY : Color.WHITE);
        int textWidth = g2d.getFontMetrics()
                           .stringWidth(text);
        g2d.drawString(text, button.x + button.width / 2 - textWidth / 2, button.y + button.height / 2 + 9);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        chatInputActive = chatInputBox.contains(e.getPoint());

        if (chatScrollArea.contains(e.getPoint()) && maxChatScroll > 0) {
            isDraggingChat = true;
            lastDragPoint = e.getPoint();
            return;
        }

        if (playerScrollArea.contains(e.getPoint()) && maxPlayerScroll > 0) {
            isDraggingPlayer = true;
            lastDragPoint = e.getPoint();
            return;
        }

        if (readyButton.contains(e.getPoint())) {

        }

        if (startButton.contains(e.getPoint())) {
            networkManager.startGame();
        }

        if (leaveButton.contains(e.getPoint())) {
            networkManager.leaveLobby();
            OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
            onlineState.setType(OnlineType.LOBBY_LIST);
        }

        if (sendButton.contains(e.getPoint()) && !chatInput.isEmpty()) {
            sendChatMessage();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDraggingChat = false;
        isDraggingPlayer = false;
        lastDragPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastDragPoint != null) {
            int deltaY = lastDragPoint.y - e.getY();

            if (isDraggingChat) {
                chatScrollOffset = Math.max(0, Math.min(chatScrollOffset + deltaY, maxChatScroll));
            }
            else if (isDraggingPlayer) {
                playerScrollOffset = Math.max(0, Math.min(playerScrollOffset + deltaY, maxPlayerScroll));
            }

            lastDragPoint = e.getPoint();
        }
    }

    public void scrollChat(int amount) {
        chatScrollOffset = Math.max(0, Math.min(chatScrollOffset + amount, maxChatScroll));
    }

    public void scrollPlayers(int amount) {
        playerScrollOffset = Math.max(0, Math.min(playerScrollOffset + amount, maxPlayerScroll));
    }

    private void sendChatMessage() {
        if (!chatInput.trim()
                      .isEmpty()) {
            networkManager.sendChatMessage(chatInput.trim());
            chatInput = "";
        }
    }

    public void addCharToChat(char c) {
        if (chatInputActive && chatInput.length() < 100) {
            chatInput += c;
        }
    }

    public void removeCharFromChat() {
        if (chatInputActive && !chatInput.isEmpty()) {
            chatInput = chatInput.substring(0, chatInput.length() - 1);
        }
    }

    public boolean isChatActive() {
        return chatInputActive;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
