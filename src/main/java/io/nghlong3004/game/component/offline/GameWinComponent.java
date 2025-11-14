package io.nghlong3004.game.component.offline;

import io.nghlong3004.game.component.button.GameButton;
import io.nghlong3004.game.component.button.SpriteButton;
import io.nghlong3004.game.context.GameContext;
import io.nghlong3004.game.context.state.PlayingState;
import io.nghlong3004.model.type.GameStateType;
import io.nghlong3004.model.type.PlayType;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static io.nghlong3004.constant.ButtonConstant.URM_BUTTON_SIZE;
import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;
import static io.nghlong3004.constant.GameConstant.SCALE;

@Slf4j
public class GameWinComponent extends PlayComponent {
    private SpriteButton replayButton;
    private SpriteButton homeButton;

    private final List<GameButton> buttons;

    private BufferedImage winImage;
    private int animationTick = 0;
    private float imageAlpha = 0f;
    private float imageScale = 0.5f;
    private float starScale = 0f;
    private int starRotation = 0;

    public GameWinComponent(GameContext context) {
        super(context);
        loadImages();
        createSpritesButton();
        buttons = List.of(replayButton, homeButton);
    }

    private void loadImages() {
        try {
            winImage = ImageIO.read(getClass().getResourceAsStream("/images/component/win.png"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to load win image");
        }
    }

    public void resetAnimation() {
        animationTick = 0;
        imageAlpha = 0f;
        imageScale = 0.5f;
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
                ((PlayingState) context.getGameState(GameStateType.OFFLINE)).setType(PlayType.PLAYING);

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
        if (imageAlpha < 1f) {
            imageAlpha += 0.02f;
            if (imageAlpha > 1f) {
                imageAlpha = 1f;
            }
        }

        if (animationTick < 40) {
            imageScale += 0.035f;
            if (imageScale > 1.2f) {
                imageScale = 1.2f;
            }
        }
        else if (animationTick < 60) {
            imageScale -= 0.01f;
            if (imageScale < 1.0f) {
                imageScale = 1.0f;
            }
        }
        else {

            float pulseAmount = (float) Math.sin(animationTick * 0.05) * 0.03f;
            imageScale = 1.0f + pulseAmount;
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
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (winImage != null) {
            int originalWidth = winImage.getWidth();
            int originalHeight = winImage.getHeight();

            int scaledWidth = (int) (originalWidth * SCALE * imageScale);
            int scaledHeight = (int) (originalHeight * SCALE * imageScale);

            int imageX = (GAME_WIDTH - scaledWidth) / 2;
            int imageY = (int) (120 * SCALE);

            int alpha = (int) (imageAlpha * 255);
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha);

            if (imageAlpha > 0.5f) {
                AlphaComposite shadowComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha * 0.3f);
                g2d.setComposite(shadowComposite);
                g2d.drawImage(winImage, imageX + 4, imageY + 4, scaledWidth, scaledHeight, null);
            }

            g2d.setComposite(alphaComposite);
            g2d.drawImage(winImage, imageX, imageY, scaledWidth, scaledHeight, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            if (starScale > 0.1f && imageAlpha > 0.5f) {
                drawStar(g2d, imageX - 40, imageY + scaledHeight / 2, 20, starScale, starRotation,
                         new Color(255, 215, 0, (int) (imageAlpha * 200)));
                drawStar(g2d, imageX + scaledWidth + 20, imageY + scaledHeight / 2, 20, starScale, -starRotation,
                         new Color(255, 215, 0, (int) (imageAlpha * 200)));
            }
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
