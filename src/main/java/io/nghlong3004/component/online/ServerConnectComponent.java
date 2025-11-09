package io.nghlong3004.component.online;

import io.nghlong3004.component.GameComponent;
import io.nghlong3004.configuration.Configuration;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.OnlineState;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.network.NetworkManager;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.OnlineType;

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
    private boolean connecting;
    private String errorMessage;

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
        int buttonWidth = (int) (200 * SCALE);
        int buttonHeight = (int) (60 * SCALE);
        int inputWidth = (int) (500 * SCALE);
        int inputHeight = (int) (55 * SCALE);

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
        if (connecting && networkManager.isConnected()) {
            connecting = false;
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
        renderImageButton(g2d, backButton, "Back", backHovered, false);

        if (errorMessage != null) {
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (20 * SCALE)));
            g2d.setColor(new Color(231, 76, 60));
            int errorWidth = g2d.getFontMetrics()
                                .stringWidth(errorMessage);
            g2d.fillRoundRect(GAME_WIDTH / 2 - errorWidth / 2 - 20, nameInputBox.y + nameInputBox.height + 20,
                              errorWidth + 40, 50, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString(errorMessage, GAME_WIDTH / 2 - errorWidth / 2, nameInputBox.y + nameInputBox.height + 50);
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

    private void renderLabel(Graphics2D g2d, String text, int x, int y) {
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (22 * SCALE)));
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
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
        errorMessage = null;

        String serverUrl = configuration.getServerUrl(selectedServerIndex + 1);
        networkManager.getCurrentLobby();
        OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
        onlineState.setType(OnlineType.LOBBY_LIST);
        connecting = false;
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
