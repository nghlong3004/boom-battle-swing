package io.nghlong3004.game.component.online;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.*;
import io.nghlong3004.websocket.model.ChatMessage;
import io.nghlong3004.websocket.model.Lobby;
import io.nghlong3004.websocket.model.PlayerInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.nghlong3004.constant.GameConstant.*;

public class LobbyRoomComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final Rectangle startButton;
    private final Rectangle readyButton;
    private final Rectangle leaveButton;
    private final Rectangle chatInputBox;
    private final Rectangle sendButton;
    private final Rectangle chatScrollArea;
    private final Rectangle playerPanelArea;
    private final Rectangle mapPreviewArea;
    private final Rectangle skinPreviewArea;
    private final Rectangle mapLeftButton;
    private final Rectangle mapRightButton;
    private final Rectangle skinPanelLeftButton;
    private final Rectangle skinPanelRightButton;
    private final BufferedImage arrowLeft;
    private final BufferedImage arrowRight;
    private Rectangle skinLeftButton;
    private Rectangle skinRightButton;
    private String chatInput = "";
    private boolean chatInputActive = false;
    private int chatScrollOffset = 0;
    private int maxChatScroll = 0;
    private Point lastDragPoint = null;
    private boolean isDraggingChat = false;

    public LobbyRoomComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();
        this.arrowLeft = ImageLoader.loadImage(ImageConstant.ARROW_LEFT);
        this.arrowRight = ImageLoader.loadImage(ImageConstant.ARROW_RIGHT);

        int sideMargin = (int) (40 * SCALE);
        int colGap = (int) (20 * SCALE);
        int topMargin = (int) (90 * SCALE);
        int bottomMargin = (int) (40 * SCALE);
        int usableHeight = Math.max(0, GAME_HEIGHT - topMargin - bottomMargin);
        int totalInnerWidth = GAME_WIDTH - sideMargin * 2 - colGap;
        int leftWidth = (int) (totalInnerWidth * 0.7);
        int rightWidth = totalInnerWidth - leftWidth;

        int rightX = sideMargin + leftWidth + colGap;

        playerPanelArea = new Rectangle(sideMargin, topMargin, leftWidth, usableHeight);
        int mapPreviewHeight = (int) (usableHeight * 0.3);
        int sectionGap = (int) (12 * SCALE);
        int chatInputHeight = (int) (44 * SCALE);
        int chatScrollHeight = Math.max(0, usableHeight - mapPreviewHeight - sectionGap - chatInputHeight - sectionGap);

        int innerGap = (int) (12 * SCALE);
        int halfWidth = Math.max(0, (rightWidth - innerGap) / 2);
        skinPreviewArea = new Rectangle(rightX, topMargin, halfWidth, mapPreviewHeight);
        mapPreviewArea = new Rectangle(rightX + halfWidth + innerGap, topMargin, rightWidth - halfWidth - innerGap,
                                       mapPreviewHeight);
        chatScrollArea = new Rectangle(rightX, topMargin + mapPreviewHeight + sectionGap, rightWidth, chatScrollHeight);

        int sendBtnWidth = (int) (40 * SCALE);
        int inputGap = (int) (10 * SCALE);
        chatInputBox = new Rectangle(rightX, chatScrollArea.y + chatScrollArea.height + sectionGap,
                                     rightWidth - sendBtnWidth - inputGap, chatInputHeight);
        sendButton = new Rectangle(chatInputBox.x + chatInputBox.width + inputGap, chatInputBox.y, sendBtnWidth,
                                   chatInputHeight);

        int buttonWidth = (int) (120 * SCALE);
        int buttonHeight = (int) (30 * SCALE);
        int controlY = usableHeight + topMargin + 10;

        leaveButton = new Rectangle(sideMargin, controlY, (int) (140 * SCALE), buttonHeight);
        readyButton = new Rectangle(GAME_WIDTH - sideMargin - (buttonWidth * 2 + inputGap), controlY, buttonWidth,
                                    buttonHeight);
        startButton = new Rectangle(GAME_WIDTH - sideMargin - buttonWidth, controlY, buttonWidth, buttonHeight);

        int arrowSize = (int) (12 * SCALE);
        mapLeftButton = new Rectangle(mapPreviewArea.x + 5, mapPreviewArea.y + (mapPreviewArea.height - arrowSize) / 2,
                                      arrowSize, arrowSize);
        mapRightButton = new Rectangle(mapPreviewArea.x + mapPreviewArea.width - arrowSize - 5,
                                       mapPreviewArea.y + (mapPreviewArea.height - arrowSize) / 2, arrowSize,
                                       arrowSize);
        skinPanelLeftButton = new Rectangle(skinPreviewArea.x + 5,
                                            skinPreviewArea.y + (skinPreviewArea.height - arrowSize) / 2, arrowSize,
                                            arrowSize);
        skinPanelRightButton = new Rectangle(skinPreviewArea.x + skinPreviewArea.width - arrowSize - 5,
                                             skinPreviewArea.y + (skinPreviewArea.height - arrowSize) / 2, arrowSize,
                                             arrowSize);
    }

    @Override
    public void update() {
        // Network updates centralized in OnlineState.update() now.
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
        renderPlayerSlots(g2d, lobby);
        renderChatArea(g2d);
        renderSkinPreview(g2d, lobby);
        renderMapPreview(g2d, lobby);
        renderChatInput(g2d);

        boolean isHost = isLocalHost(lobby);
        boolean isReady = isLocalReady(lobby);
        boolean allReady = true;
        for (PlayerInfo p : lobby.getPlayers()) {
            if (!p.isReady()) {
                allReady = false;
                break;
            }
        }
        boolean startDisabled = !isHost || lobby.getPlayers()
                                                .isEmpty() || !allReady;

        renderButton(g2d, readyButton, isReady ? "Unready" : "Ready", new Color(46, 204, 113), isHost);
        renderButton(g2d, startButton, "Start", new Color(52, 152, 219), startDisabled);
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
        String info = String.format("Map: %s  •  Players: %d/%d", lobby.getNameMap(), lobby.getPlayers()
                                                                                           .size(),
                                    lobby.getMaxPlayers());
        g2d.drawString(info, 50, 105);
    }

    private final Map<String, BufferedImage> avatarCache = new HashMap<>();
    private final Map<String, BufferedImage> mapPreviewCache = new HashMap<>();

    private void renderPlayerSlots(Graphics2D g2d, Lobby lobby) {
        int panelX = playerPanelArea.x;
        int panelY = playerPanelArea.y;
        int panelWidth = playerPanelArea.width;
        int panelHeight = playerPanelArea.height;

        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("Room Members", panelX + 20, panelY + 40);

        int cols = 2;
        int rows = 2;
        int titleSpace = (int) (40 * SCALE);
        int hSpace = (int) (16 * SCALE);
        int vSpace = (int) (8 * SCALE);

        int gridW = panelWidth - (cols + 1) * hSpace;
        int gridH = panelHeight - titleSpace - (rows + 1) * vSpace;
        int slotW = Math.max((int) (140 * SCALE), gridW / cols);
        int slotH = Math.max((int) (110 * SCALE), gridH / rows);

        List<PlayerInfo> players = lobby.getPlayers();

        skinLeftButton = null;
        skinRightButton = null;

        for (int i = 0; i < 4; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = panelX + hSpace + col * (slotW + hSpace);
            int y = panelY + titleSpace + vSpace + row * (slotH + vSpace);

            g2d.setColor(new Color(45, 52, 65, 200));
            g2d.fillRoundRect(x, y, slotW, slotH, 12, 12);
            g2d.setColor(new Color(80, 90, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, slotW, slotH, 12, 12);

            if (i < players.size()) {
                PlayerInfo p = players.get(i);

                BufferedImage avatar = getAvatar(p.getSkinType());
                int avatarW = Math.min((int) (80 * SCALE), slotW / 3);
                int avatarH = Math.min((int) (110 * SCALE), slotH - 20);
                int ax = x + 15;
                int ay = y + (slotH - avatarH) / 2;
                if (avatar != null) {
                    g2d.drawImage(avatar, ax, ay, avatarW, avatarH, null);
                }

                g2d.setFont(new Font("Arial", Font.BOLD, (int) (22 * SCALE)));
                g2d.setColor(p.isHost() ? new Color(255, 215, 0) : Color.WHITE);
                String name = p.getPlayerName();
                g2d.drawString(name, x + avatarW + 30, y + 35);

                g2d.setFont(new Font("Arial", Font.PLAIN, (int) (18 * SCALE)));
                g2d.setColor(new Color(180, 180, 200));
                g2d.drawString("Skin: " + p.getSkinType(), x + avatarW + 30, y + 60);

                g2d.setFont(new Font("Arial", Font.BOLD, (int) (16 * SCALE)));
                if (p.isReady()) {
                    g2d.setColor(new Color(46, 204, 113));
                    g2d.drawString("Ready", x + avatarW + 30, y + 85);
                }
                else {
                    g2d.setColor(new Color(231, 76, 60));
                    g2d.drawString("Not Ready", x + avatarW + 30, y + 85);
                }
            }
            else {
                g2d.setFont(new Font("Arial", Font.ITALIC, (int) (20 * SCALE)));
                g2d.setColor(Color.GRAY);
                g2d.drawString("Waiting...", x + 20, y + slotH / 2);
            }
        }
    }

    private void renderArrowButton(Graphics2D g2d, Rectangle rect, boolean left) {
        if (left) {
            g2d.drawImage(arrowLeft, rect.x, rect.y, rect.width, rect.height, null);
        }
        else {
            g2d.drawImage(arrowRight, rect.x, rect.y, rect.width, rect.height, null);
        }
    }

    private BufferedImage getAvatar(String skinKey) {
        if (skinKey == null || skinKey.isBlank()) {
            return null;
        }
        return avatarCache.computeIfAbsent(skinKey, k -> ImageLoader.loadImage(
                ImageConstant.BOMBER_AVATAR_TEMPLATE.formatted(k)));
    }

    private void renderMapPreview(Graphics2D g2d, Lobby lobby) {
        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(mapPreviewArea.x, mapPreviewArea.y, mapPreviewArea.width, mapPreviewArea.height, 15, 15);
        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(mapPreviewArea.x, mapPreviewArea.y, mapPreviewArea.width, mapPreviewArea.height, 15, 15);

        BufferedImage mapImg = getMapPreview(lobby.getMapType());
        if (mapImg != null) {
            g2d.drawImage(mapImg, mapPreviewArea.x + 5, mapPreviewArea.y + 5, mapPreviewArea.width - 10,
                          mapPreviewArea.height - 10, null);
        }

        boolean isHost = isLocalHost(lobby);
        if (isHost) {
            renderArrowButton(g2d, mapLeftButton, true);
            renderArrowButton(g2d, mapRightButton, false);
        }
    }

    private BufferedImage getMapPreview(String mapKey) {
        if (mapKey == null || mapKey.isBlank()) {
            return null;
        }
        return mapPreviewCache.computeIfAbsent(mapKey,
                                               k -> ImageLoader.loadImage(MapConstant.MAP_PATH_TEMPLATE.formatted(k)));
    }

    private void renderSkinPreview(Graphics2D g2d, Lobby lobby) {
        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(skinPreviewArea.x, skinPreviewArea.y, skinPreviewArea.width, skinPreviewArea.height, 15, 15);
        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(skinPreviewArea.x, skinPreviewArea.y, skinPreviewArea.width, skinPreviewArea.height, 15, 15);

        PlayerInfo me = getLocalPlayer(lobby);
        BufferedImage avatar = me != null ? getAvatar(me.getSkinType()) : null;
        if (avatar != null) {
            int padding = 10;
            int availW = skinPreviewArea.width - padding * 2;
            int availH = skinPreviewArea.height - padding * 2;
            int drawW = Math.min(availW, (int) (availH * 0.7));
            int drawH = Math.min(availH, (int) (drawW * 1.4));
            int dx = skinPreviewArea.x + (skinPreviewArea.width - drawW) / 2;
            int dy = skinPreviewArea.y + (skinPreviewArea.height - drawH) / 2;
            g2d.drawImage(avatar, dx, dy, drawW, drawH, null);
        }

        if (me != null) {
            renderArrowButton(g2d, skinPanelLeftButton, true);
            renderArrowButton(g2d, skinPanelRightButton, false);
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
            renderScrollbar(g2d, chatX + chatWidth - 10, chatY + 55, chatHeight - 65, chatScrollOffset, maxChatScroll);
        }
    }

    private void renderScrollbar(Graphics2D g2d, int x, int y, int height, int offset, int maxOffset) {
        g2d.setColor(new Color(60, 70, 85, 150));
        g2d.fillRoundRect(x, y, 8, height, 8, 8);

        if (maxOffset > 0) {
            int scrollbarHeight = Math.max(30, (int) (height * ((float) height / (height + maxOffset))));
            int scrollbarY = y + (int) ((height - scrollbarHeight) * ((float) offset / maxOffset));

            g2d.setColor(new Color(100, 200, 255));
            g2d.fillRoundRect(x, scrollbarY, 8, scrollbarHeight, 8, 8);
        }
    }

    public void handleWheel(int amount, Point p) {
        int notchPixels = (amount * 20);
        if (chatScrollArea.contains(p)) {
            scrollChat(notchPixels);
        }
        else {
            if (p.x > playerPanelArea.x + playerPanelArea.width) {
                scrollChat(notchPixels);
            }
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

        playerPanelArea.contains(e.getPoint());

        Lobby lobby = networkManager.getCurrentLobby();
        if (lobby != null) {
            if (readyButton.contains(e.getPoint())) {
                PlayerInfo me = getLocalPlayer(lobby);
                if (me != null && !me.isHost()) {
                    boolean newReady = !me.isReady();
                    if (networkManager.isConnected()) {
                        networkManager.toggleReady(newReady);
                    }
                    else {
                        me.setReady(newReady);
                    }
                }
            }

            if (startButton.contains(e.getPoint())) {
                boolean allReady = true;
                for (PlayerInfo p : lobby.getPlayers()) {
                    if (!p.isReady()) {
                        allReady = false;
                        break;
                    }
                }
                if (isLocalHost(lobby) && lobby.getPlayers()
                                               .size() >= 1 && allReady) {
                    if (!networkManager.isConnected()) {
                        int missing = Math.max(0, 4 - lobby.getPlayers()
                                                           .size());
                        if (missing > 0) {
                            SkinType[] skins = SkinType.values();
                            int base = (int) (System.currentTimeMillis() % skins.length);
                            for (int i = 0; i < missing; i++) {
                                String pid = "bot_" + System.currentTimeMillis() + "_" + i;
                                String pname = "Bot" + (i + 1);
                                String skin = skins[(base + i) % skins.length].getAssetKey();
                                lobby.getPlayers()
                                     .add(new PlayerInfo(pid, pname, skin, true, false));
                            }
                            if (networkManager.getAvailableLobbies() != null) {
                                for (Lobby l : networkManager.getAvailableLobbies()) {
                                    if (l.getLobbyId() != null && l.getLobbyId()
                                                                   .equals(lobby.getLobbyId())) {
                                        int need = Math.max(0, 4 - l.getPlayers()
                                                                    .size());
                                        for (int i = 0; i < need; i++) {
                                            String pid = "bot_" + System.currentTimeMillis() + "_l_" + i;
                                            String pname = "Bot" + (i + 1);
                                            String skin = skins[(base + i) % skins.length].getAssetKey();
                                            l.getPlayers()
                                             .add(new PlayerInfo(pid, pname, skin, true, false));
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        MapType selectedMap = MapType.DESERT_MODE;
                        for (MapType mt : MapType.values()) {
                            if (mt.getAssetKey()
                                  .equalsIgnoreCase(lobby.getMapType())) {
                                selectedMap = mt;
                                break;
                            }
                        }
                        PlayerInfo me = getLocalPlayer(lobby);
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
                        context.setMapType(selectedMap);
                        context.setGameType(GameType.OFFLINE);
                        context.setPlayerCount(PlayerCountType.ONE_PLAYER);
                        context.setNumberBomber(1);
                        SkinType[] skinsArr = context.getSkinType();
                        if (skinsArr == null || skinsArr.length < 2) {
                            skinsArr = new SkinType[2];
                        }
                        skinsArr[0] = mySkin;
                        context.setSkinType(skinsArr);
                        context.changeState(GameStateType.PLAYING);
                    }
                    networkManager.startGame();
                }
            }

            if (leaveButton.contains(e.getPoint())) {
                networkManager.leaveLobby();
                OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
                onlineState.setType(OnlineType.LOBBY_LIST);
            }

            if (sendButton.contains(e.getPoint()) && !chatInput.isEmpty()) {
                sendChatMessage();
            }

            if (isLocalHost(lobby)) {
                if (mapLeftButton.contains(e.getPoint())) {
                    cycleMap(lobby, -1);
                }
                else if (mapRightButton.contains(e.getPoint())) {
                    cycleMap(lobby, 1);
                }
            }
            PlayerInfo me = getLocalPlayer(lobby);
            if (me != null && skinLeftButton != null && skinRightButton != null) {
                if (skinLeftButton.contains(e.getPoint())) {
                    cycleSkin(me, -1);
                }
                else if (skinRightButton.contains(e.getPoint())) {
                    cycleSkin(me, 1);
                }
            }

            if (me != null) {
                if (skinPanelLeftButton != null && skinPanelLeftButton.contains(e.getPoint())) {
                    cycleSkin(me, -1);
                }
                else if (skinPanelRightButton != null && skinPanelRightButton.contains(e.getPoint())) {
                    cycleSkin(me, 1);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDraggingChat = false;
        lastDragPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastDragPoint != null) {
            int deltaY = lastDragPoint.y - e.getY();

            if (isDraggingChat) {
                chatScrollOffset = Math.max(0, Math.min(chatScrollOffset + deltaY, maxChatScroll));
            }

            lastDragPoint = e.getPoint();
        }
    }

    public void scrollChat(int amount) {
        chatScrollOffset = Math.max(0, Math.min(chatScrollOffset + amount, maxChatScroll));
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

    private PlayerInfo getLocalPlayer(Lobby lobby) {
        String playerId = networkManager.getPlayerId();
        String playerName = networkManager.getPlayerName();
        PlayerInfo me = null;
        if (playerId != null) {
            for (PlayerInfo p : lobby.getPlayers()) {
                if (playerId.equals(p.getPlayerId())) {
                    me = p;
                    break;
                }
            }
        }
        if (me == null && playerName != null) {
            for (PlayerInfo p : lobby.getPlayers()) {
                if (playerName.equals(p.getPlayerName())) {
                    me = p;
                    break;
                }
            }
        }
        if (me == null) {
            for (PlayerInfo p : lobby.getPlayers()) {
                if ("You".equalsIgnoreCase(p.getPlayerName()) || "you".equalsIgnoreCase(p.getPlayerId())) {
                    me = p;
                    break;
                }
            }
        }
        return me;
    }

    private boolean isLocalHost(Lobby lobby) {
        PlayerInfo me = getLocalPlayer(lobby);
        return me != null && me.isHost();
    }

    private boolean isLocalReady(Lobby lobby) {
        PlayerInfo me = getLocalPlayer(lobby);
        return me != null && me.isReady();
    }

    private void cycleMap(Lobby lobby, int dir) {
        MapType[] values = MapType.values();
        int idx = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].getAssetKey()
                         .equalsIgnoreCase(lobby.getMapType())) {
                idx = i;
                break;
            }
        }
        int next = (idx + dir + values.length) % values.length;
        String nextKey = values[next].getAssetKey();
        if (networkManager.isConnected()) {
            networkManager.changeMap(nextKey);
        }
        else {
            lobby.setMapType(nextKey);
        }
    }

    private void cycleSkin(PlayerInfo me, int dir) {
        SkinType[] values = SkinType.values();
        int idx = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].getAssetKey()
                         .equalsIgnoreCase(me.getSkinType())) {
                idx = i;
                break;
            }
        }
        int next = (idx + dir + values.length) % values.length;
        String nextKey = values[next].getAssetKey();
        if (networkManager.isConnected()) {
            networkManager.changeSkin(nextKey);
        }
        else {
            me.setSkinType(nextKey);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
