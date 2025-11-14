package io.nghlong3004.game.component.online;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.OnlineState;
import io.nghlong3004.game.manager.NetworkManager;
import io.nghlong3004.model.Room;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.OnlineType;
import io.nghlong3004.model.type.SkinType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.GameConstant.*;

public class RoomListComponent extends GameComponent {

    private final NetworkManager networkManager;
    private final CreateRoomDialog createRoomDialog;
    private final List<Rectangle> roomButtons;
    private final Rectangle createButton;
    private final Rectangle refreshButton;
    private final Rectangle backButton;
    private int hoveredroomIndex = -1;
    private static final boolean OFFLINE_TEST_MODE = false;
    private static final int OFFLINE_MOCK_PLAYERS = 3;

    public RoomListComponent(GameContext context) {
        super(context);
        this.networkManager = NetworkManager.getInstance();
        this.createRoomDialog = new CreateRoomDialog();
        this.roomButtons = new ArrayList<>();

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
        if (networkManager.getCurrentRoom() != null) {
            OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
            onlineState.setType(OnlineType.ROOM);
        }
        updateRoomButtons();
    }

    private void updateRoomButtons() {
        roomButtons.clear();
        var rooms = networkManager.getAvailableRooms();

        int listMarginX = (int) (80 * SCALE);
        int listTop = (int) (140 * SCALE);
        int listBottom = (int) (GAME_HEIGHT - 150 * SCALE);
        int areaHeight = Math.max(0, listBottom - listTop);

        int itemHeight = (int) (60 * SCALE);
        int itemSpacing = (int) (20 * SCALE);
        int listWidth = GAME_WIDTH - listMarginX * 2;
        int listX = (GAME_WIDTH - listWidth) / 2;

        int n = rooms.size();
        int totalHeight = n > 0 ? n * itemHeight + (n - 1) * itemSpacing : 0;
        int startY = listTop + Math.max(0, (areaHeight - totalHeight) / 2);

        for (int i = 0; i < n; i++) {
            Rectangle button = new Rectangle(listX, startY + i * (itemHeight + itemSpacing), listWidth, itemHeight);
            roomButtons.add(button);
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
        String title = "Game rooms";
        int titleWidth = g2d.getFontMetrics()
                            .stringWidth(title);
        g2d.drawString(title, GAME_WIDTH / 2 - titleWidth / 2, 80);

        var rooms = networkManager.getAvailableRooms();
        for (int i = 0; i < rooms.size() && i < roomButtons.size(); i++) {
            renderroomButton(g2d, roomButtons.get(i), rooms.get(i), i == hoveredroomIndex);
        }

        if (rooms.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.ITALIC, (int) (28 * SCALE)));
            g2d.setColor(Color.GRAY);
            String norooms = "No rooms available. Create one to start!";
            int width = g2d.getFontMetrics()
                           .stringWidth(norooms);
            g2d.drawString(norooms, GAME_WIDTH / 2 - width / 2, GAME_HEIGHT / 2);
        }

        renderButton(g2d, createButton, "Create", new Color(46, 204, 113));
        renderButton(g2d, refreshButton, "Refresh", new Color(52, 152, 219));
        renderButton(g2d, backButton, "Back", new Color(231, 76, 60));

        createRoomDialog.render(g2d);
    }

    private void renderroomButton(Graphics2D g2d, Rectangle rect, Room room, boolean hovered) {
        Color bgColor = hovered ? new Color(45, 52, 65) : new Color(30, 35, 45);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        Color borderColor = hovered ? new Color(100, 200, 255) : new Color(60, 70, 85);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 15, 15);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (28 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString(room.getName(), rect.x + 20, rect.y + 35);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (20 * SCALE)));
        g2d.setColor(new Color(180, 180, 200));
        String info = String.format("Players: %d/%d | Map: %s", room.getBomberInfos()
                                                                    .size(), room.getMaxBomber(), room.getMap()
                                                                                                      .getName());
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
        if (createRoomDialog.isVisible()) {
            if (createRoomDialog.isCreateClicked(e)) {
                createNewRoom();
            }
            createRoomDialog.handleMousePressed(e);
            return;
        }

        if (createButton.contains(e.getPoint())) {
            createRoomDialog.show();
            return;
        }

        if (refreshButton.contains(e.getPoint())) {
            networkManager.requestRoomList();
        }

        if (backButton.contains(e.getPoint())) {
            networkManager.disconnect();
            context.changeState(GameStateType.MENU);
        }

        List<Room> rooms = networkManager.getAvailableRooms();
        for (int i = 0; i < roomButtons.size() && i < rooms.size(); i++) {
            if (roomButtons.get(i)
                           .contains(e.getPoint())) {
                Room room = rooms.get(i);

                if (networkManager.isConnected()) {
                    networkManager.joinRoom(room.getId());
                }
                break;
            }
        }
    }

    private void createNewRoom() {
        String roomName = createRoomDialog.getRoomName();
        MapType mapType = createRoomDialog.getSelectedMap();
        SkinType skinType = createRoomDialog.getSelectedSkin();

        if (networkManager.isConnected()) {
            networkManager.createRoom(roomName, 4, mapType, skinType);
        }

        createRoomDialog.hide();

        OnlineState onlineState = (OnlineState) context.getGameState(GameStateType.ONLINE);
        onlineState.setType(OnlineType.ROOM);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (createRoomDialog.isVisible()) {
            createRoomDialog.handleMouseMoved(e);
            return;
        }

        hoveredroomIndex = -1;
        List<Room> rooms = networkManager.getAvailableRooms();
        for (int i = 0; i < roomButtons.size() && i < rooms.size(); i++) {
            if (roomButtons.get(i)
                           .contains(e.getPoint())) {
                hoveredroomIndex = i;
                break;
            }
        }
    }

    public boolean isDialogInputActive() {
        return createRoomDialog.isVisible() && createRoomDialog.isNameInputActive();
    }

    public void addCharToDialog(char c) {
        createRoomDialog.addCharToName(c);
    }

    public void removeCharFromDialog() {
        createRoomDialog.removeCharFromName();
    }
}
