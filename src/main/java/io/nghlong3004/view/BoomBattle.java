package io.nghlong3004.view;

import io.nghlong3004.view.state.StateContext;
import lombok.Builder;

import java.awt.*;

@Builder
public class BoomBattle {

    private final StateContext context;
    private final GamePanel gamePanel;
    private final GameFrame gameFrame;
    private final Thread thread;

    public void start() {
        thread.start();
    }

    public void shutdown() throws InterruptedException {
        thread.join();
    }

    public void render(Graphics g) {
        context.render(g);
    }

    public void update() {
        context.update();
    }
}
