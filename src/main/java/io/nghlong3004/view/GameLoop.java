package io.nghlong3004.view;

import io.nghlong3004.view.state.StateContext;
import lombok.extern.slf4j.Slf4j;

import static io.nghlong3004.constant.FrameRateConstant.FPS_SET;
import static io.nghlong3004.constant.FrameRateConstant.UPS_SET;

@Slf4j
public class GameLoop implements Runnable {
    private final double nsPerUpdate;
    private final double nsPerFrame;
    private final StateContext context;
    private final GamePanel gamePanel;
    private long prevNs;
    private double accU, accF;

    private long fpsTimerMs;
    private int frames, updates;

    private final int MAX_CATCHUP_UPDATES;

    public GameLoop(StateContext context, GamePanel gamePanel) {
        this.context = context;
        this.gamePanel = gamePanel;
        this.nsPerUpdate = 1e9 / UPS_SET;
        this.nsPerFrame = 1e9 / FPS_SET;
        this.MAX_CATCHUP_UPDATES = UPS_SET * 2;
    }

    @Override
    public void run() {
        initClocks();
        while (!Thread.currentThread().isInterrupted()) {
            final long elapsed = stepAndGetElapsedNs();

            accumulate(elapsed);

            int catchup = performCatchUpUpdates();
            if (catchup == MAX_CATCHUP_UPDATES) {
                accU = 0;
            }

            performRenderIfDue();

            logAndResetFpsIfNeeded();

            throttleOrYield();
        }
    }


    private void initClocks() {
        prevNs = System.nanoTime();
        accU = accF = 0.0;
        fpsTimerMs = System.currentTimeMillis();
        frames = updates = 0;
    }

    private long stepAndGetElapsedNs() {
        final long now = System.nanoTime();
        final long elapsed = now - prevNs;
        prevNs = now;
        return elapsed;
    }

    private void accumulate(long elapsedNs) {
        accU += elapsedNs;
        accF += elapsedNs;
    }

    private int performCatchUpUpdates() {
        int catchups = 0;
        while (accU >= nsPerUpdate && catchups < MAX_CATCHUP_UPDATES) {
            context.update();
            updates++;
            accU -= nsPerUpdate;
            catchups++;
        }
        return catchups;
    }

    private void performRenderIfDue() {
        if (accF >= nsPerFrame) {
            gamePanel.repaint();
            frames++;
            accF -= nsPerFrame;
            if (accF >= nsPerFrame) {
                accF = 0;
            }
        }
    }

    private void logAndResetFpsIfNeeded() {
        long nowMs = System.currentTimeMillis();
        if (nowMs - fpsTimerMs >= 1000) {
            fpsTimerMs += 1000;
            log.debug("FPS: {} | UPS: {}", frames, updates);
            frames = 0;
            updates = 0;
        }
    }

    private void throttleOrYield() {
        final double nextFrameIn = nsPerFrame - accF;
        final double nextUpdateIn = nsPerUpdate - accU;
        long sleepNs = (long) Math.min(nextFrameIn, nextUpdateIn);

        if (sleepNs > 0) {
            long sleepMillis = sleepNs / 1_000_000;

            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        else {
            Thread.yield();
        }
    }
}