package io.nghlong3004.game.component.option;

import io.nghlong3004.game.component.GameComponent;
import io.nghlong3004.game.component.button.VolumeButton;
import io.nghlong3004.game.context.GameContext;

import java.awt.*;
import java.awt.event.MouseEvent;

import static io.nghlong3004.constant.ButtonConstant.SLIDER_BUTTON;
import static io.nghlong3004.constant.ButtonConstant.VOLUME_BUTTON_HEIGHT;
import static io.nghlong3004.constant.GameConstant.*;

public class AudioComponent extends GameComponent {
    private VolumeButton volumeButton;

    private final Rectangle musicLabelBounds;
    private final Rectangle sfxLabelBounds;
    private boolean musicHover, sfxHover;
    private boolean musicPressed, sfxPressed;
    private boolean musicMuted, sfxMuted;

    public AudioComponent(GameContext context) {
        super(context);
        createVolumeButton();

        musicMuted = context.getAudio()
                            .isSongMuted();
        sfxMuted = context.getAudio()
                          .isEffectMuted();

        musicLabelBounds = new Rectangle(0, 0, 1, 1);
        sfxLabelBounds = new Rectangle(0, 0, 1, 1);
    }

    private void createVolumeButton() {
        int vX = GAME_WIDTH - SLIDER_BUTTON >>> 1;
        int vY = (int) (262 * SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_BUTTON, VOLUME_BUTTON_HEIGHT, context.getAudio());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (musicLabelBounds.contains(e.getX(), e.getY())) {
            musicPressed = true;
        }
        else if (sfxLabelBounds.contains(e.getX(), e.getY())) {
            sfxPressed = true;
        }
        else if (volumeButton.isMouseOver(e)) {
            volumeButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (musicLabelBounds.contains(e.getX(), e.getY()) && musicPressed) {
            musicMuted = !musicMuted;
            context.getAudio()
                   .toggleSongMute();
        }
        else if (sfxLabelBounds.contains(e.getX(), e.getY()) && sfxPressed) {
            sfxMuted = !sfxMuted;
            context.getAudio()
                   .toggleEffectMute();
        }

        musicPressed = false;
        sfxPressed = false;
        volumeButton.reset();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valueBefore = volumeButton.getFloatValue();
            volumeButton.changeButtonX(e.getX());
            float valueAfter = volumeButton.getFloatValue();
            if (valueBefore != valueAfter) {
                context.getAudio()
                       .setVolume(valueAfter);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        musicHover = musicLabelBounds.contains(e.getX(), e.getY());
        sfxHover = sfxLabelBounds.contains(e.getX(), e.getY());
        volumeButton.setMouseOver(volumeButton.isMouseOver(e));
    }

    @Override
    public void update() {
        volumeButton.update();
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int fontSize = (int) (28 * SCALE);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();

        int centerX = GAME_WIDTH / 2;
        int spacing = (int) (70 * SCALE);

        String musicText = "MUSIC";
        int musicTextWidth = fm.stringWidth(musicText);
        int musicTextX = centerX - musicTextWidth / 2;
        int musicTextY = GAME_HEIGHT / 4;
        renderClickableLabel(g2d, musicText, musicTextX, musicTextY, musicTextWidth, fontSize, fm, musicHover,
                             musicPressed, musicMuted, musicLabelBounds);

        String sfxText = "SFX";
        int sfxTextWidth = fm.stringWidth(sfxText);
        int sfxTextX = centerX - sfxTextWidth / 2;
        int sfxTextY = musicTextY + spacing;
        renderClickableLabel(g2d, sfxText, sfxTextX, sfxTextY, sfxTextWidth, fontSize, fm, sfxHover, sfxPressed,
                             sfxMuted, sfxLabelBounds);

        volumeButton.render(g);
    }

    private void renderClickableLabel(Graphics2D g2d, String text, int textX, int textY, int textWidth, int fontSize,
                                      FontMetrics fm, boolean hover, boolean pressed, boolean muted, Rectangle bounds) {
        int padding = (int) (12 * SCALE);
        int bgX = textX - padding;
        int bgY = textY - fm.getAscent() - padding / 2;
        int bgWidth = textWidth + padding * 2;
        int bgHeight = fontSize + padding;
        int arc = (int) (15 * SCALE);

        bounds.setBounds(bgX, bgY, bgWidth, bgHeight);

        g2d.setColor(new Color(0, 0, 0, pressed ? 80 : 120));
        g2d.fillRoundRect(bgX + (pressed ? 2 : 4), bgY + (pressed ? 2 : 4), bgWidth, bgHeight, arc, arc);

        Color topColor, bottomColor;
        if (pressed) {
            topColor = new Color(35, 42, 44, 240);
            bottomColor = new Color(75, 85, 90, 240);
        }
        else if (hover) {
            topColor = new Color(55, 62, 64, 235);
            bottomColor = new Color(115, 125, 130, 235);
        }
        else {
            topColor = new Color(45, 52, 54, 230);
            bottomColor = new Color(99, 110, 114, 230);
        }

        GradientPaint gradient = new GradientPaint(bgX, bgY, topColor, bgX, bgY + bgHeight, bottomColor);
        g2d.setPaint(gradient);
        g2d.fillRoundRect(bgX, bgY, bgWidth, bgHeight, arc, arc);

        g2d.setStroke(new BasicStroke(2.5f * SCALE));
        Color borderColor = hover ? new Color(150, 220, 255, 220) : new Color(116, 185, 255, 180);
        g2d.setColor(borderColor);
        g2d.drawRoundRect(bgX, bgY, bgWidth, bgHeight, arc, arc);

        g2d.setColor(new Color(255, 255, 255, hover ? 50 : 30));
        g2d.fillRoundRect(bgX + 3, bgY + 3, bgWidth - 6, bgHeight / 2 - 3, arc - 3, arc - 3);

        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawString(text, textX + 2, textY + 2);

        Color textTop = hover ? new Color(255, 255, 255) : new Color(240, 245, 250);
        Color textBottom = hover ? new Color(210, 240, 255) : new Color(190, 220, 245);
        GradientPaint textGradient = new GradientPaint(textX, textY - fontSize, textTop, textX, textY, textBottom);
        g2d.setPaint(textGradient);
        g2d.drawString(text, textX, textY);

        if (muted) {
            g2d.setStroke(new BasicStroke(3.5f * SCALE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int lineY = textY - fontSize / 3;
            int lineStartX = textX - padding / 2;
            int lineEndX = textX + textWidth + padding / 2;

            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.drawLine(lineStartX + 2, lineY + 2, lineEndX + 2, lineY + 2);

            g2d.setColor(new Color(255, 60, 60, 230));
            g2d.drawLine(lineStartX, lineY, lineEndX, lineY);
        }
    }
}
