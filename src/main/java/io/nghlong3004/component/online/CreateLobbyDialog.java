package io.nghlong3004.component.online;

import io.nghlong3004.loader.ImageLoader;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.GameConstant.*;
import static io.nghlong3004.constant.ImageConstant.BUTTON;
import static io.nghlong3004.constant.ImageConstant.BUTTON_TOUCH;

public class CreateLobbyDialog {
    
    private static final String[] AVAILABLE_MAPS = {
        "Desert", "Town", "Underwater", "Land", "Xmas"
    };
    
    private static final String[] AVAILABLE_SKINS = {
        "boz", "evie", "ike", "plunk"
    };
    
    private Rectangle dialogBox;
    private Rectangle lobbyNameInputBox;
    private Rectangle createButton;
    private Rectangle cancelButton;
    private Rectangle[] mapButtons;
    private Rectangle[] skinButtons;
    
    private String lobbyName = "";
    private int selectedMapIndex = 0;
    private int selectedSkinIndex = 0;
    private boolean nameInputActive = false;
    private boolean visible = false;
    
    private int hoveredMapIndex = -1;
    private int hoveredSkinIndex = -1;
    private boolean createHovered = false;
    private boolean cancelHovered = false;
    
    private java.awt.image.BufferedImage buttonImage;
    private java.awt.image.BufferedImage buttonTouchImage;
    
    public CreateLobbyDialog() {
        int dialogWidth = (int) (700 * SCALE);
        int dialogHeight = (int) (550 * SCALE);
        int dialogX = (GAME_WIDTH - dialogWidth) / 2;
        int dialogY = (GAME_HEIGHT - dialogHeight) / 2;
        
        dialogBox = new Rectangle(dialogX, dialogY, dialogWidth, dialogHeight);
        
        int inputWidth = (int) (600 * SCALE);
        int inputHeight = (int) (50 * SCALE);
        lobbyNameInputBox = new Rectangle(dialogX + 50, dialogY + 80, inputWidth, inputHeight);
        
        mapButtons = new Rectangle[AVAILABLE_MAPS.length];
        int mapButtonWidth = (int) (120 * SCALE);
        int mapButtonHeight = (int) (45 * SCALE);
        int mapStartX = dialogX + 50;
        int mapY = dialogY + 200;
        for (int i = 0; i < AVAILABLE_MAPS.length; i++) {
            mapButtons[i] = new Rectangle(mapStartX + i * (mapButtonWidth + 10), mapY, mapButtonWidth, mapButtonHeight);
        }
        
        skinButtons = new Rectangle[AVAILABLE_SKINS.length];
        int skinButtonWidth = (int) (140 * SCALE);
        int skinButtonHeight = (int) (45 * SCALE);
        int skinStartX = dialogX + 50;
        int skinY = dialogY + 330;
        for (int i = 0; i < AVAILABLE_SKINS.length; i++) {
            skinButtons[i] = new Rectangle(skinStartX + i * (skinButtonWidth + 10), skinY, skinButtonWidth, skinButtonHeight);
        }
        
        int buttonWidth = (int) (200 * SCALE);
        int buttonHeight = (int) (55 * SCALE);
        int buttonY = dialogY + dialogHeight - 80;
        createButton = new Rectangle(dialogX + 100, buttonY, buttonWidth, buttonHeight);
        cancelButton = new Rectangle(dialogX + dialogWidth - 300, buttonY, buttonWidth, buttonHeight);
        
        loadImages();
    }
    
    private void loadImages() {
        buttonImage = ImageLoader.loadImage(BUTTON);
        buttonTouchImage = ImageLoader.loadImage(BUTTON_TOUCH);
    }
    
    public void show() {
        visible = true;
        lobbyName = "";
        selectedMapIndex = 0;
        selectedSkinIndex = 0;
        nameInputActive = false;
    }
    
    public void hide() {
        visible = false;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void render(Graphics2D g2d) {
        if (!visible) return;
        
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        
        g2d.setColor(new Color(25, 30, 40));
        g2d.fillRoundRect(dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height, 20, 20);
        
        g2d.setColor(new Color(100, 200, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height, 20, 20);
        
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (32 * SCALE)));
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("Create New Lobby", dialogBox.x + 50, dialogBox.y + 45);
        
        renderLabel(g2d, "Lobby Name:", lobbyNameInputBox.x, lobbyNameInputBox.y - 25);
        renderInputBox(g2d);
        
        renderLabel(g2d, "Select Map:", mapButtons[0].x, mapButtons[0].y - 25);
        renderMapButtons(g2d);
        
        renderLabel(g2d, "Select Character:", skinButtons[0].x, skinButtons[0].y - 25);
        renderSkinButtons(g2d);
        
        renderImageButton(g2d, createButton, "Create", createHovered, lobbyName.trim().isEmpty());
        renderImageButton(g2d, cancelButton, "Cancel", cancelHovered, false);
    }
    
    private void renderLabel(Graphics2D g2d, String text, int x, int y) {
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (20 * SCALE)));
        g2d.setColor(new Color(180, 190, 200));
        g2d.drawString(text, x, y);
    }
    
    private void renderInputBox(Graphics2D g2d) {
        g2d.setColor(nameInputActive ? new Color(45, 52, 65) : new Color(35, 40, 50));
        g2d.fillRoundRect(lobbyNameInputBox.x, lobbyNameInputBox.y, 
                         lobbyNameInputBox.width, lobbyNameInputBox.height, 12, 12);
        
        g2d.setColor(nameInputActive ? new Color(100, 200, 255) : new Color(80, 90, 100));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(lobbyNameInputBox.x, lobbyNameInputBox.y, 
                         lobbyNameInputBox.width, lobbyNameInputBox.height, 12, 12);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, (int) (24 * SCALE)));
        String displayText = lobbyName.isEmpty() ? "Enter lobby name..." : lobbyName;
        g2d.setColor(lobbyName.isEmpty() ? Color.GRAY : Color.WHITE);
        g2d.drawString(displayText + (nameInputActive && !lobbyName.isEmpty() ? "|" : ""), 
                      lobbyNameInputBox.x + 15, lobbyNameInputBox.y + 33);
    }
    
    private void renderMapButtons(Graphics2D g2d) {
        for (int i = 0; i < mapButtons.length; i++) {
            Rectangle btn = mapButtons[i];
            boolean selected = selectedMapIndex == i;
            boolean hovered = hoveredMapIndex == i;
            
            if (selected) {
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            } else if (hovered) {
                g2d.setColor(new Color(60, 70, 80));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            } else {
                g2d.setColor(new Color(40, 45, 55));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            
            g2d.setColor(selected ? new Color(100, 255, 150) : (hovered ? new Color(100, 200, 255) : new Color(80, 90, 100)));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (18 * SCALE)));
            g2d.setColor(Color.WHITE);
            int textWidth = g2d.getFontMetrics().stringWidth(AVAILABLE_MAPS[i]);
            g2d.drawString(AVAILABLE_MAPS[i], btn.x + btn.width / 2 - textWidth / 2, btn.y + 28);
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
            } else if (hovered) {
                g2d.setColor(new Color(60, 70, 80));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            } else {
                g2d.setColor(new Color(40, 45, 55));
                g2d.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            }
            
            g2d.setColor(selected ? new Color(100, 200, 255) : (hovered ? new Color(100, 200, 255) : new Color(80, 90, 100)));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 10, 10);
            
            g2d.setFont(new Font("Arial", Font.BOLD, (int) (18 * SCALE)));
            g2d.setColor(Color.WHITE);
            String skinName = AVAILABLE_SKINS[i].substring(0, 1).toUpperCase() + AVAILABLE_SKINS[i].substring(1);
            int textWidth = g2d.getFontMetrics().stringWidth(skinName);
            g2d.drawString(skinName, btn.x + btn.width / 2 - textWidth / 2, btn.y + 28);
        }
    }
    
    private void renderImageButton(Graphics2D g2d, Rectangle button, String text, boolean hovered, boolean disabled) {
        java.awt.image.BufferedImage img = hovered && !disabled ? buttonTouchImage : buttonImage;
        g2d.drawImage(img, button.x, button.y, button.width, button.height, null);
        
        g2d.setFont(new Font("Arial", Font.BOLD, (int) (26 * SCALE)));
        g2d.setColor(disabled ? Color.GRAY : Color.WHITE);
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, button.x + button.width / 2 - textWidth / 2, 
                      button.y + button.height / 2 + 10);
    }
    
    public boolean handleMousePressed(MouseEvent e) {
        if (!visible) return false;
        
        nameInputActive = lobbyNameInputBox.contains(e.getPoint());
        
        for (int i = 0; i < mapButtons.length; i++) {
            if (mapButtons[i].contains(e.getPoint())) {
                selectedMapIndex = i;
                return true;
            }
        }
        
        for (int i = 0; i < skinButtons.length; i++) {
            if (skinButtons[i].contains(e.getPoint())) {
                selectedSkinIndex = i;
                return true;
            }
        }
        
        if (createButton.contains(e.getPoint()) && !lobbyName.trim().isEmpty()) {
            return false;
        }
        
        if (cancelButton.contains(e.getPoint())) {
            hide();
            return true;
        }
        
        return dialogBox.contains(e.getPoint());
    }
    
    public void handleMouseMoved(MouseEvent e) {
        if (!visible) return;
        
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
        return visible && createButton.contains(e.getPoint()) && !lobbyName.trim().isEmpty();
    }
    
    public boolean isNameInputActive() {
        return visible && nameInputActive;
    }
    
    public void addCharToName(char c) {
        if (nameInputActive && lobbyName.length() < 30) {
            lobbyName += c;
        }
    }
    
    public void removeCharFromName() {
        if (nameInputActive && !lobbyName.isEmpty()) {
            lobbyName = lobbyName.substring(0, lobbyName.length() - 1);
        }
    }
    
    public String getLobbyName() {
        return lobbyName.trim();
    }
    
    public String getSelectedMap() {
        return AVAILABLE_MAPS[selectedMapIndex];
    }
    
    public String getSelectedSkin() {
        return AVAILABLE_SKINS[selectedSkinIndex];
    }
}
