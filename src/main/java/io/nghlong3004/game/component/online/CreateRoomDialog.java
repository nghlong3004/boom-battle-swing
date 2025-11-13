package io.nghlong3004.game.component.online;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.loader.ImageLoader;
import io.nghlong3004.model.type.MapType;
import io.nghlong3004.model.type.SkinType;
import lombok.Getter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BUTTON;
import static io.nghlong3004.constant.ImageConstant.BUTTON_TOUCH;

public class CreateRoomDialog {

    private final MapType[] availableMaps;

    private final SkinType[] availableSkins;

    private final Rectangle dialogBox;
    private final Rectangle roomNameInputBox;
    private final Rectangle createButton;
    private final Rectangle cancelButton;
    private final Rectangle[] mapButtons;
    private final Rectangle[] skinButtons;
    private final BufferedImage[] mapImages;
    private final BufferedImage[] skinImages;

    @Getter
    private String roomName = "";
    private int selectedMapIndex = 0;
    private int selectedSkinIndex = 0;
    private boolean nameInputActive = false;
    @Getter
    private boolean visible = false;

    private int hoveredMapIndex = -1;
    private int hoveredSkinIndex = -1;
    private boolean createHovered = false;
    private boolean cancelHovered = false;

    private java.awt.image.BufferedImage buttonImage;
    private java.awt.image.BufferedImage buttonTouchImage;

    public CreateRoomDialog() {
        this.availableMaps = MapType.values();
        this.availableSkins = SkinType.values();
        int dialogWidth = (int) (GAME_WIDTH * 0.6);
        int dialogHeight = (int) (GAME_HEIGHT * 0.8);
        int dialogX = (GAME_WIDTH - dialogWidth) / 2;
        int dialogY = (GAME_HEIGHT - dialogHeight) / 2;

        this.dialogBox = new Rectangle(dialogX, dialogY, dialogWidth, dialogHeight);

        int inputWidth = (int) (dialogWidth * 0.8);
        int inputHeight = (int) (30 * SCALE);
        int inputX = GAME_WIDTH - inputWidth >>> 1;
        int inputY = dialogY + 30;
        this.roomNameInputBox = new Rectangle(inputX, inputY, inputWidth, inputHeight);

        this.mapButtons = new Rectangle[this.availableMaps.length];
        int mapButtonWidth = (int) (dialogWidth / (this.availableMaps.length + 1));
        int mapButtonHeight = (int) (80 * SCALE);
        int mapStartX = dialogX + mapButtonWidth / (this.availableMaps.length + 1);
        int mapY = dialogY + inputHeight * 2;
        this.mapImages = new BufferedImage[this.availableMaps.length];
        for (int i = 0; i < this.availableMaps.length; i++) {
            this.mapButtons[i] = new Rectangle(
                    mapStartX + i * (mapButtonWidth + mapButtonWidth / (this.availableMaps.length + 1)), mapY,
                    mapButtonWidth, mapButtonHeight);
            this.mapImages[i] = ImageLoader.loadImage(
                    MapConstant.MAP_PATH_TEMPLATE.formatted(this.availableMaps[i].getAssetKey()));
        }

        this.skinButtons = new Rectangle[this.availableSkins.length];
        int skinButtonWidth = (int) (dialogWidth / (this.availableSkins.length + 1));
        int skinButtonHeight = (int) (80 * SCALE);
        int skinStartX = dialogX + (skinButtonWidth / (this.availableSkins.length + 1));
        int skinY = mapY + mapButtonHeight * 3 / 2;
        this.skinImages = new BufferedImage[this.availableSkins.length];
        for (int i = 0; i < this.availableSkins.length; i++) {
            this.skinButtons[i] = new Rectangle(
                    skinStartX + i * (skinButtonWidth + (skinButtonWidth / (this.availableSkins.length + 1))), skinY,
                    skinButtonWidth, skinButtonHeight);
            this.skinImages[i] = ImageLoader.loadImage(
                    ImageConstant.BOMBER_AVATAR_TEMPLATE.formatted(availableSkins[i].getAssetKey()));
        }

        int buttonWidth = (int) (120 * SCALE);
        int buttonHeight = (int) (45 * SCALE);
        int buttonY = dialogY + dialogHeight - 80;
        this.createButton = new Rectangle(dialogX + 100, buttonY, buttonWidth, buttonHeight);
        this.cancelButton = new Rectangle(dialogX + dialogWidth - 300, buttonY, buttonWidth, buttonHeight);


        loadImages();
    }

    private void loadImages() {
        buttonImage = ImageLoader.loadImage(BUTTON);
        buttonTouchImage = ImageLoader.loadImage(BUTTON_TOUCH);
    }

    public void show() {
        visible = true;
        roomName = "";
        selectedMapIndex = 0;
        selectedSkinIndex = 0;
        nameInputActive = false;
    }

    public void hide() {
        visible = false;
    }

    public void render(Graphics2D g2d) {
        if (!visible) {
            return;
        }

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g2d.setColor(new Color(25, 30, 40));
        g2d.fillRoundRect(dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height, 20, 20);

        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height, 20, 20);

        renderInputBox(g2d);

        renderMapButtons(g2d);

        renderSkinButtons(g2d);

        renderImageButton(g2d, cancelButton, "Cancel", cancelHovered, false);

        renderImageButton(g2d, createButton, "Create", createHovered, roomName.trim()
                                                                              .isEmpty());
    }

    private void renderInputBox(Graphics2D g2d) {
        g2d.setColor(nameInputActive ? new Color(45, 52, 65) : new Color(35, 40, 50));
        g2d.fillRoundRect(roomNameInputBox.x, roomNameInputBox.y, roomNameInputBox.width, roomNameInputBox.height, 12,
                          12);

        g2d.setColor(nameInputActive ? new Color(100, 200, 255) : new Color(80, 90, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(roomNameInputBox.x, roomNameInputBox.y, roomNameInputBox.width, roomNameInputBox.height, 12,
                          12);

        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (24 * SCALE)));
        String displayText = roomName.isEmpty() ? "Enter room name..." : roomName;
        g2d.setColor(roomName.isEmpty() ? Color.GRAY : Color.WHITE);
        g2d.drawString(displayText + (nameInputActive && !roomName.isEmpty() ? "|" : ""), roomNameInputBox.x + 15,
                       roomNameInputBox.y + 33);
    }

    private void renderMapButtons(Graphics2D g2d) {
        for (int i = 0; i < mapButtons.length; i++) {
            Rectangle btn = mapButtons[i];
            boolean selected = selectedMapIndex == i;
            boolean hovered = hoveredMapIndex == i;

            if (selected) {
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            else if (hovered) {
                g2d.setColor(new Color(60, 70, 80));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            else {
                g2d.setColor(new Color(40, 45, 55));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }

            g2d.setColor(selected ? new Color(100, 255, 150)
                                  : (hovered ? new Color(100, 200, 255) : new Color(80, 90, 100)));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            g2d.drawImage(mapImages[i], btn.x + 3, btn.y + 3, btn.width - 3, btn.height - 3, null);
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (13 * SCALE)));
            g2d.setColor(Color.WHITE);
            String text = availableMaps[i].getName();
            int textWidth = g2d.getFontMetrics()
                               .stringWidth(text);
            g2d.drawString(text, btn.x + btn.width / 2 - textWidth / 2, btn.y + btn.height + 20);
        }
    }

    private void renderSkinButtons(Graphics2D g2d) {
        for (int i = 0; i < skinButtons.length; i++) {
            Rectangle btn = skinButtons[i];
            boolean selected = selectedSkinIndex == i;
            boolean hovered = hoveredSkinIndex == i;

            if (selected) {
                g2d.setColor(new Color(52, 152, 219));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            else if (hovered) {
                g2d.setColor(new Color(60, 70, 80));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            else {
                g2d.setColor(new Color(40, 45, 55));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }

            g2d.setColor(selected ? new Color(100, 200, 255)
                                  : (hovered ? new Color(100, 200, 255) : new Color(80, 90, 100)));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            g2d.drawImage(skinImages[i], btn.x, btn.y, btn.width, btn.height, null);
        }
    }

    private void renderImageButton(Graphics2D g2d, Rectangle button, String text, boolean hovered, boolean disabled) {
        java.awt.image.BufferedImage img = hovered && !disabled ? buttonTouchImage : buttonImage;
        g2d.drawImage(img, button.x, button.y, button.width, button.height, null);

        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(disabled ? Color.GRAY : Color.WHITE);
        int textWidth = g2d.getFontMetrics()
                           .stringWidth(text);
        g2d.drawString(text, button.x + button.width / 2 - textWidth / 2, button.y + button.height / 2 + 10);
    }

    public void handleMousePressed(MouseEvent e) {
        if (!visible) {
            return;
        }

        nameInputActive = roomNameInputBox.contains(e.getPoint());

        for (int i = 0; i < mapButtons.length; i++) {
            if (mapButtons[i].contains(e.getPoint())) {
                selectedMapIndex = i;
                return;
            }
        }

        for (int i = 0; i < skinButtons.length; i++) {
            if (skinButtons[i].contains(e.getPoint())) {
                selectedSkinIndex = i;
                return;
            }
        }

        if (createButton.contains(e.getPoint()) && !roomName.trim()
                                                            .isEmpty()) {
            return;
        }

        if (cancelButton.contains(e.getPoint())) {
            hide();
            return;
        }

        dialogBox.contains(e.getPoint());
    }

    public void handleMouseMoved(MouseEvent e) {
        if (!visible) {
            return;
        }

        hoveredMapIndex = -1;
        for (int i = 0; i < mapButtons.length; i++) {
            if (mapButtons[i].contains(e.getPoint())) {
                hoveredMapIndex = i;
                break;
            }
        }

        hoveredSkinIndex = -1;
        for (int i = 0; i < skinButtons.length; i++) {
            if (skinButtons[i].contains(e.getPoint())) {
                hoveredSkinIndex = i;
                break;
            }
        }

        createHovered = createButton.contains(e.getPoint());
        cancelHovered = cancelButton.contains(e.getPoint());
    }

    public boolean isCreateClicked(MouseEvent e) {
        return visible && createButton.contains(e.getPoint()) && !roomName.trim()
                                                                          .isEmpty();
    }

    public boolean isNameInputActive() {
        return visible && nameInputActive;
    }

    public void addCharToName(char c) {
        if (nameInputActive && roomName.length() < 30) {
            roomName += c;
        }
    }

    public void removeCharFromName() {
        if (nameInputActive && !roomName.isEmpty()) {
            roomName = roomName.substring(0, roomName.length() - 1);
        }
    }

    public MapType getSelectedMap() {
        return availableMaps[selectedMapIndex];
    }

    public SkinType getSelectedSkin() {
        return availableSkins[selectedSkinIndex];
    }
}
