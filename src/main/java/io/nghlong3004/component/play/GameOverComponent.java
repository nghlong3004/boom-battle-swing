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
public class GameOverComponent extends PlayComponent {
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;


    private int animationTick = 0;
    private float textAlpha = 0f;
    private float textScale = 0.5f;
    private boolean scaleGrowing = true;

    public GameOverComponent(GameContext context) {
        super(context);
        createSpritesButton();
        buttons = List.of(replayButton, homeButton);
    }

    public void resetAnimation() {
        animationTick = 0;
        textAlpha = 0f;
        textScale = 0.5f;
        scaleGrowing = true;
    }

    private void createSpritesButton() {
        int buttonSpacing = (int) (URM_BUTTON_SIZE * 6 / 5);
        int totalWidth = URM_BUTTON_SIZE * 2 + buttonSpacing;
        int startX = (GAME_WIDTH - totalWidth + URM_BUTTON_SIZE) / 2;
        int spriteY = (int) (350 * SCALE);

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
                PlayingState playingState = (PlayingState) context.getGameState(GameStateType.PLAYING);
                if (playingState instanceof PlayingState) {
                    ((GamePlayComponent) playingState.getComponent(PlayType.PLAYING)).exit();
                }
                context.changeState(GameStateType.MENU);
            }
        }
        else if (replayButton.isMouseOver(e)) {
            if (replayButton.isMousePressed()) {
                PlayingState playingState = (PlayingState) context.getGameState(GameStateType.PLAYING);
                if (playingState instanceof PlayingState) {
                    ((GamePlayComponent) playingState.getComponent(PlayType.PLAYING)).play();
                }
                playingState.setType(PlayType.PLAYING);
                resetAnimation();
                log.info("Game reset from Game Over, restarting...");
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
                scaleGrowing = false;
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
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String gameOverText = "GAME OVER";
        int baseFontSize = (int) (60 * SCALE);
        int scaledFontSize = (int) (baseFontSize * textScale);
        Font gameOverFont = new Font("Arial", Font.BOLD, scaledFontSize);
        g2d.setFont(gameOverFont);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);
        int textHeight = fm.getHeight();
        int textX = (GAME_WIDTH - textWidth) / 2;
        int textY = (int) (200 * SCALE);

        int alpha = (int) (textAlpha * 255);
        Color color1 = new Color(255, 50, 50, alpha);
        Color color2 = new Color(200, 0, 0, alpha);

        GradientPaint gradient = new GradientPaint(textX, textY - textHeight, color1, textX, textY, color2);

        if (textAlpha > 0.5f) {
            g2d.setColor(new Color(0, 0, 0, (int) (textAlpha * 100)));
            g2d.drawString(gameOverText, textX + 3, textY + 3);
        }

        g2d.setPaint(gradient);
        g2d.drawString(gameOverText, textX, textY);

        for (var gameObject : buttons) {
            gameObject.render(g);
        }
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
