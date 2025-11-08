package io.nghlong3004.manager;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static io.nghlong3004.constant.GameConstant.GAME_WIDTH;
import static io.nghlong3004.constant.GameConstant.UPS;

public class GameTimer {
    private static final int INITIAL_TIME_SECONDS = 120;
    private int remainingTicks;
    private boolean timeUp;
    private BufferedImage timeBorder;

    @Getter
    private boolean running;

    public GameTimer() {
        loadTimeBorder();
        reset();
    }

    private void loadTimeBorder() {
        try {
            timeBorder = ImageIO.read(getClass().getResourceAsStream("/images/map/time_border.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        remainingTicks = INITIAL_TIME_SECONDS * UPS;
        timeUp = false;
        running = false;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void update() {
        if (!running || timeUp) {
            return;
        }

        remainingTicks--;
        if (remainingTicks <= 0) {
            remainingTicks = 0;
            timeUp = true;
        }
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public int getRemainingSeconds() {
        return remainingTicks / UPS;
    }

    public void render(Graphics g) {
        int minutes = getRemainingSeconds() / 60;
        int seconds = getRemainingSeconds() % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);

        int borderWidth = 120;
        int borderHeight = 50;
        int x = (GAME_WIDTH - borderWidth) / 2;
        int y = 10;

        if (timeBorder != null) {
            g.drawImage(timeBorder, x, y, borderWidth, borderHeight, null);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(x, y, borderWidth, borderHeight, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, borderWidth, borderHeight, 10, 10);
        }

        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(timeText);
        int textX = x + (borderWidth - textWidth) / 2;
        int textY = y + ((borderHeight - fm.getHeight()) / 2) + fm.getAscent();

        if (getRemainingSeconds() <= 10) {
            g.setColor(Color.RED);
        } else if (getRemainingSeconds() <= 30) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.WHITE);
        }

        g.drawString(timeText, textX, textY);
    }
}
