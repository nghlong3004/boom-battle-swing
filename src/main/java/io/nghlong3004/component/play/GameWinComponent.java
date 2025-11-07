package io.nghlong3004.component.play;

import io.nghlong3004.component.button.GameButton;
import io.nghlong3004.component.button.SpriteButton;
import io.nghlong3004.context.GameContext;
import io.nghlong3004.context.state.PlayingState;
import io.nghlong3004.type.GameStateType;
import io.nghlong3004.type.PlayType;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;
import static io.nghlong3004.constant.GameConstant.SCALE;

@Slf4j
public class GameWinComponent extends PlayComponent {
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    private int animationTick = 0;
    private float textAlpha = 0f;
    private float textScale = 0.5f;
    private float starScale = 0f;
    private int starRotation = 0;

    public GameWinComponent(GameContext context) {
        super(context);
        createSpritesButton();
        buttons = List.of(replayButton, homeButton);
    }

    public void resetAnimation() {
        animationTick = 0;
        textAlpha = 0f;
        textScale = 0.5f;
        starScale = 0f;
        starRotation = 0;
    }

    private void createSpritesButton() {
        int buttonSpacing = (int) (URM_BUTTON_SIZE * 6 / 5);
        int totalWidth = URM_BUTTON_SIZE * 2 + buttonSpacing;
        int startX = (GAME_WIDTH - totalWidth + URM_BUTTON_SIZE) / 2;
        int spriteY = (int) (380 * SCALE);

        homeButton = new SpriteButton(startX, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
        replayButton = new SpriteButton(startX + buttonSpacing, spriteY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (homeButton.isMouseOver(e)) {
            if (homeButton.isMousePressed()) {
                context.changeState(GameStateType.MENU);
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                ((PlayingState) context.getGameState(GameStateType.PLAYING)).setType(PlayType.PLAYING);

            }
        }

        for (var gameButton : buttons) {
            gameButton.reset();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (var gameButton : buttons) {
            gameButton.setMouseOver(false);
        }
        for (var gameButton : buttons) {
            if (gameButton.isMouseOver(e)) {
                gameButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void update() {
        for (var gameObject : buttons) {
            gameObject.update();
        }
        animationTick++;
        if (textAlpha < 1f) {
            textAlpha += 0.02f;
            if (textAlpha > 1f) {
                textAlpha = 1f;
            }
        }

        if (animationTick < 40) {
            textScale += 0.035f;
            if (textScale > 1.2f) {
                textScale = 1.2f;
            }
        }
        else if (animationTick < 60) {
            textScale -= 0.01f;
            if (textScale < 1.0f) {
                textScale = 1.0f;
            }
        }
        else {

            float pulseAmount = (float) Math.sin(animationTick * 0.05) * 0.03f;
            textScale = 1.0f + pulseAmount;
        }

        if (starScale < 1f) {
            starScale += 0.03f;
            if (starScale > 1f) {
                starScale = 1f;
            }
        }

        starRotation = (starRotation + 2) % 360;
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String victoryText = "VICTORY!";
        int baseFontSize = (int) (70 * SCALE);
        int scaledFontSize = (int) (baseFontSize * textScale);
        Font victoryFont = new Font("Arial", Font.BOLD, scaledFontSize);
        g2d.setFont(victoryFont);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(victoryText);
        int textHeight = fm.getHeight();
        int textX = (GAME_WIDTH - textWidth) / 2;
        int textY = (int) (180 * SCALE);

        int alpha = (int) (textAlpha * 255);
        Color color1 = new Color(255, 215, 0, alpha);
        Color color2 = new Color(255, 165, 0, alpha);

        GradientPaint gradient = new GradientPaint(textX, textY - textHeight, color1, textX, textY, color2);


        if (textAlpha > 0.5f) {
            g2d.setColor(new Color(0, 0, 0, (int) (textAlpha * 120)));
            g2d.drawString(victoryText, textX + 4, textY + 4);
        }


        g2d.setPaint(gradient);
        g2d.drawString(victoryText, textX, textY);


        if (starScale > 0.1f) {
            drawStar(g2d, textX - 40, textY - 40, 20, starScale, starRotation,
                     new Color(255, 215, 0, (int) (textAlpha * 200)));
            drawStar(g2d, textX + textWidth + 20, textY - 40, 20, starScale, -starRotation,
                     new Color(255, 215, 0, (int) (textAlpha * 200)));
        }


        if (textAlpha > 0.7f) {
            Font subtitleFont = new Font("Arial", Font.PLAIN, (int) (30 * SCALE));
            g2d.setFont(subtitleFont);
            g2d.setColor(new Color(255, 255, 255, (int) (textAlpha * 200)));
            String subtitle = "You Win!";
            FontMetrics subFm = g2d.getFontMetrics();
            int subWidth = subFm.stringWidth(subtitle);
            g2d.drawString(subtitle, (GAME_WIDTH - subWidth) / 2, (int) (250 * SCALE));
        }


        for (var gameObject : buttons) {
            gameObject.render(g);
        }
    }

    private void drawStar(Graphics2D g2d, int x, int y, int size, float scale, int rotation, Color color) {
        g2d.setColor(color);

        int scaledSize = (int) (size * scale);
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        double angle = Math.toRadians(rotation);

        for (int i = 0; i < 10; i++) {
            double r = (i % 2 == 0) ? scaledSize : scaledSize / 2.5;
            double theta = angle + (i * Math.PI / 5) - Math.PI / 2;
            xPoints[i] = (int) (x + r * Math.cos(theta));
            yPoints[i] = (int) (y + r * Math.sin(theta));
        }

        g2d.fillPolygon(xPoints, yPoints, 10);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
