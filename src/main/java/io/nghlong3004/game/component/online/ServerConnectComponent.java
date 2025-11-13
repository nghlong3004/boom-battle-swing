package io.nghlong3004.game.component.online;

import io.nghlong3004.configuration.Configuration;
import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.OnlineType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BUTTON;
import static io.nghlong3004.constant.ImageConstant.BUTTON_TOUCH;

public class ServerConnectComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final Configuration configuration;
    private int selectedServerIndex;
    private String playerName;
    private volatile boolean connecting;
    private volatile long connectStartTime;
    private volatile String errorMessage;
    private volatile long errorExpireAt;
    private volatile boolean cancelConnect;
    private Thread connectThread;

    private BufferedImage buttonImage;
    private BufferedImage buttonTouchImage;

    private Rectangle connectButton;
    private Rectangle backButton;
    private Rectangle serverSelectorBox;
    private List<Rectangle> serverDropdownItems;
    private Rectangle nameInputBox;

    private boolean nameInputActive;
    private boolean serverDropdownOpen;
    private int hoveredDropdownIndex;
    private boolean connectHovered;
    private boolean backHovered;

    public ServerConnectComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();
        this.configuration = Configuration.getInstance();
        this.playerName = "";
        this.connecting = false;
        this.connectStartTime = 0L;
        this.errorExpireAt = 0L;
        this.cancelConnect = false;
        this.connectThread = null;
        this.selectedServerIndex = 0;
        this.hoveredDropdownIndex = -1;
        this.serverDropdownOpen = false;

        loadImages();
        initializeComponents();
    }

    private void loadImages() {
        buttonImage = ImageLoader.loadImage(BUTTON);
        buttonTouchImage = ImageLoader.loadImage(BUTTON_TOUCH);
    }

    private void initializeComponents() {
        int centerX = GAME_WIDTH / 2;
        int buttonWidth = (int) (120 * SCALE);
        int buttonHeight = (int) (30 * SCALE);
        int inputWidth = (int) (400 * SCALE);
        int inputHeight = (int) (30 * SCALE);

        int serverSelectorY = (int) (200 * SCALE);
        serverSelectorBox = new Rectangle(centerX - inputWidth / 2, serverSelectorY, inputWidth, inputHeight);

        int serverCount = configuration.getServerCount();
        serverDropdownItems = new ArrayList<>();
        for (int i = 0; i < serverCount; i++) {
            int y = serverSelectorBox.y + serverSelectorBox.height + i * inputHeight;
            serverDropdownItems.add(new Rectangle(serverSelectorBox.x, y, serverSelectorBox.width, inputHeight));
        }

        int nameInputY = serverSelectorY + 100;
        nameInputBox = new Rectangle(centerX - inputWidth / 2, nameInputY, inputWidth, inputHeight);

        int buttonsY = nameInputY + 100;
        connectButton = new Rectangle(centerX - buttonWidth - 20, buttonsY, buttonWidth, buttonHeight);
        backButton = new Rectangle(centerX + 20, buttonsY, buttonWidth, buttonHeight);
    }

    @Override
    public void update() {
        if (connecting) {
            if (networkManager.isConnected()) {
                networkManager.requestRoomList();
                OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
                onlineState.setType(OnlineType.ROOM_LIST);
                connecting = false;
                connectThread = null;
            }
        }
        if (errorMessage != null && errorExpireAt > 0 && System.currentTimeMillis() >= errorExpireAt) {
            errorMessage = null;
            errorExpireAt = 0L;
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
        String title = "Boom Battle Online";
        int titleWidth = g2d.getFontMetrics()
                            .stringWidth(title);
        g2d.drawString(title, GAME_WIDTH / 2 - titleWidth / 2, (int) (100 * SCALE));

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (20 * SCALE)));
        g2d.setColor(Color.LIGHT_GRAY);
        String subtitle = "Select a server and enter your name";
        int subtitleWidth = g2d.getFontMetrics()
                               .stringWidth(subtitle);
        g2d.drawString(subtitle, GAME_WIDTH / 2 - subtitleWidth / 2, (int) (150 * SCALE));

        renderServerSelector(g2d);

        renderInputBox(g2d, nameInputBox, playerName.isEmpty() ? "Enter your name" : playerName, nameInputActive,
                       playerName.isEmpty());

        renderImageButton(g2d, connectButton, "Connect", connectHovered, connecting);
        renderImageButton(g2d, backButton, "Back", backHovered, connecting);

        if (errorMessage != null) {
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (20 * SCALE)));
            g2d.setColor(new Color(231, 76, 60));
            int errorWidth = g2d.getFontMetrics()
                                .stringWidth(errorMessage);
            g2d.fillRoundRect(GAME_WIDTH - errorWidth - 10 >>> 1, GAME_HEIGHT >>> 1, errorWidth + 40, 50, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString(errorMessage, GAME_WIDTH - errorWidth >>> 1, GAME_HEIGHT >>> 1);
        }

        if (connecting) {
            renderConnectingAnimation(g2d);
        }

        if (serverDropdownOpen) {
            renderDropdown(g2d);
        }
    }

    private void renderServerSelector(Graphics2D g2d) {
        g2d.setColor(new Color(40, 50, 60));
        g2d.fillRoundRect(serverSelectorBox.x, serverSelectorBox.y, serverSelectorBox.width, serverSelectorBox.height,
                          12, 12);

        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(serverSelectorBox.x, serverSelectorBox.y, serverSelectorBox.width, serverSelectorBox.height,
                          12, 12);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (24 * SCALE)));
        g2d.setColor(Color.WHITE);
        String selectedText = "Server " + (selectedServerIndex + 1);
        g2d.drawString(selectedText, serverSelectorBox.x + 15, serverSelectorBox.y + serverSelectorBox.height / 2 + 10);

        int arrowX = serverSelectorBox.x + serverSelectorBox.width - 40;
        int arrowY = serverSelectorBox.y + serverSelectorBox.height / 2;
        int[] xPoints = {arrowX, arrowX + 15, arrowX + 30};
        int[] yPoints = serverDropdownOpen ? new int[]{arrowY + 5, arrowY - 5, arrowY + 5}
                                           : new int[]{arrowY - 5, arrowY + 5, arrowY - 5};
        g2d.setColor(new Color(100, 200, 255));
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private void renderDropdown(Graphics2D g2d) {
        for (int i = 0; i < serverDropdownItems.size(); i++) {
            Rectangle item = serverDropdownItems.get(i);
            boolean hovered = hoveredDropdownIndex == i;

            g2d.setColor(hovered ? new Color(60, 70, 80) : new Color(35, 40, 50));
            g2d.fillRoundRect(item.x, item.y, item.width, item.height, 8, 8);

            g2d.setColor(hovered ? new Color(100, 200, 255) : new Color(80, 90, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(item.x, item.y, item.width, item.height, 8, 8);

            g2d.setFont(new Font("Arial", Font.PLAIN, (int) (22 * SCALE)));
            g2d.setColor(selectedServerIndex == i ? new Color(255, 215, 0) : Color.WHITE);
            String text = "Server " + (i + 1);
            g2d.drawString(text, item.x + 15, item.y + item.height / 2 + 8);
        }
    }

    private void renderInputBox(Graphics2D g2d, Rectangle box, String text, boolean active, boolean placeholder) {
        g2d.setColor(active ? new Color(60, 70, 80) : new Color(40, 50, 60));
        g2d.fillRoundRect(box.x, box.y, box.width, box.height, 12, 12);

        g2d.setColor(active ? new Color(100, 200, 255) : new Color(80, 90, 100));
        g2d.setStroke(new BasicStroke(active ? 3 : 2));
        g2d.drawRoundRect(box.x, box.y, box.width, box.height, 12, 12);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (24 * SCALE)));
        g2d.setColor(placeholder ? Color.GRAY : Color.WHITE);
        g2d.drawString(text + (active && !placeholder ? "|" : ""), box.x + 15, box.y + box.height / 2 + 10);
    }

    private void renderImageButton(Graphics2D g2d, Rectangle button, String text, boolean hovered, boolean disabled) {
        BufferedImage img = hovered && !disabled ? buttonTouchImage : buttonImage;
        g2d.drawImage(img, button.x, button.y, button.width, button.height, null);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(disabled ? Color.GRAY : Color.WHITE);
        int textWidth = g2d.getFontMetrics()
                           .stringWidth(text);
        g2d.drawString(text, button.x + button.width / 2 - textWidth / 2, button.y + button.height / 2 + 10);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (connecting) {
            return;
        }
        if (serverSelectorBox.contains(e.getPoint())) {
            serverDropdownOpen = !serverDropdownOpen;
            nameInputActive = false;
            return;
        }

        if (serverDropdownOpen) {
            boolean clickedOnDropdown = false;
            for (int i = 0; i < serverDropdownItems.size(); i++) {
                if (serverDropdownItems.get(i)
                                       .contains(e.getPoint())) {
                    selectedServerIndex = i;
                    serverDropdownOpen = false;
                    errorMessage = null;
                    clickedOnDropdown = true;
                    break;
                }
            }
            if (!clickedOnDropdown) {
                serverDropdownOpen = false;
            }
            return;
        }

        nameInputActive = nameInputBox.contains(e.getPoint());

        if (connectButton.contains(e.getPoint()) && !connecting) {
            tryConnect();
        }

        if (backButton.contains(e.getPoint())) {
            context.changeState(GameStateType.MENU);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredDropdownIndex = -1;
        if (serverDropdownOpen) {
            for (int i = 0; i < serverDropdownItems.size(); i++) {
                if (serverDropdownItems.get(i)
                                       .contains(e.getPoint())) {
                    hoveredDropdownIndex = i;
                    break;
                }
            }
        }

        connectHovered = connectButton.contains(e.getPoint());
        backHovered = backButton.contains(e.getPoint());
    }

    private void tryConnect() {
        if (playerName.trim()
                      .isEmpty()) {
            errorMessage = "Please enter your name!";
            return;
        }

        if (playerName.trim()
                      .length() < 2) {
            errorMessage = "Player name must be at least 2 characters!";
            return;
        }

        connecting = true;
        connectStartTime = System.currentTimeMillis();
        errorMessage = null;
        nameInputActive = false;
        serverDropdownOpen = false;
        final String serverUrl = configuration.getServerUrl(selectedServerIndex + 1);

        cancelConnect = false;
        connectThread = getThread(serverUrl);
        connectThread.start();
    }

    private Thread getThread(String serverUrl) {
        Thread t = new Thread(() -> {
            try {
                networkManager.connect(serverUrl, playerName.trim());
                if (cancelConnect) {
                    if (networkManager.isConnected()) {
                        networkManager.disconnect();
                    }
                    return;
                }
                if (!networkManager.isConnected()) {
                    showTransientError("Could not connect to server", 500);
                    connecting = false;
                    connectThread = null;
                }
            } finally {

            }
        }, "connect-thread");
        t.setDaemon(true);
        return t;
    }

    private void showTransientError(String msg, long durationMs) {
        errorMessage = msg;
        errorExpireAt = System.currentTimeMillis() + Math.max(0, durationMs);
    }

    public void cancelConnecting() {
        cancelConnect = true;
        if (connecting) {
            connecting = false;
        }
        if (connectThread != null && connectThread.isAlive()) {
            try {
                connectThread.interrupt();
            } catch (Exception ignored) {
            }
        }
        connectThread = null;
        if (networkManager.isConnected()) {
            networkManager.disconnect();
        }
        showTransientError("Connection cancelled", 3000);
    }

    private void renderConnectingAnimation(Graphics2D g2d) {
        long elapsed = System.currentTimeMillis() - connectStartTime;
        int dots = (int) ((elapsed / 500) % 4);
        String base = "Connecting";
        StringBuilder sb = new StringBuilder(base);
        sb.append(".".repeat(Math.max(0, dots)));
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (22 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        int textWidth = g2d.getFontMetrics()
                           .stringWidth(sb.toString());
        int y = connectButton.y + connectButton.height + (int) (40 * SCALE);
        g2d.drawString(sb.toString(), GAME_WIDTH / 2 - textWidth / 2, y);

        int spinnerSize = (int) (40 * SCALE);
        int cx = GAME_WIDTH / 2 - spinnerSize / 2;
        int cy = y + (int) (10 * SCALE);
        int angle = (int) ((elapsed / 10) % 360);
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(60, 130, 200));
        g2d.drawArc(cx, cy, spinnerSize, spinnerSize, angle, 270);
    }

    public void addCharToName(char c) {
        if (nameInputActive && playerName.length() < 20) {
            playerName += c;
        }
    }

    public void removeCharFromName() {
        if (nameInputActive && !playerName.isEmpty()) {
            playerName = playerName.substring(0, playerName.length() - 1);
        }
    }

    public boolean isAnyInputActive() {
        return nameInputActive;
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
