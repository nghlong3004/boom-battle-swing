package io.nghlong3004.boom_battle_swing.view.game;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import static io.nghlong3004.boom_battle_swing.constant.FrameRateConstant.FPS_SET;
import static io.nghlong3004.boom_battle_swing.constant.FrameRateConstant.UPS_SET;

@Slf4j
public final class GameLoop implements Runnable {

    private final GameUpdater updater;
    private final GameDrawer renderer;

    public GameLoop(GameUpdater updater, GameDrawer renderer) {
        this.updater = updater;
        this.renderer = renderer;
    }

    public void stop() {
    }

    @Override
    public void run() {
        final double nsPerUpdate = 1e9 / UPS_SET;
        final double nsPerFrame = 1e9 / FPS_SET;

        long prev = System.nanoTime();
        double accU = 0.0;
        double accF = 0.0;

        long fpsTimer = System.currentTimeMillis();
        int frames = 0, updates = 0;

        final int MAX_CATCHUP_UPDATES = UPS_SET * 2;

        while (!Thread.currentThread().isInterrupted()) {
            final long now = System.nanoTime();
            final long elapsed = now - prev;
            prev = now;

            accU += elapsed;
            accF += elapsed;

            int catchups = 0;
            while (accU >= nsPerUpdate && catchups < MAX_CATCHUP_UPDATES) {
                updater.update();
                updates++;
                accU -= nsPerUpdate;
                catchups++;
            }

            if (catchups == MAX_CATCHUP_UPDATES) {
                accU = 0;
            }

            if (accF >= nsPerFrame) {
                renderer.draw();
                frames++;
                accF -= nsPerFrame;
                if (accF >= nsPerFrame) {
                    accF = 0;
                }
                Toolkit.getDefaultToolkit().sync();
            }

            if (System.currentTimeMillis() - fpsTimer >= 1000) {
                fpsTimer += 1000;
                log.debug("FPS: {} | UPS: {}", frames, updates);
                frames = 0;
                updates = 0;
            }

            final double nextFrameIn = nsPerFrame - accF;
            final double nextUpdateIn = nsPerUpdate - accU;
            long sleepNs = (long) Math.min(nextFrameIn, nextUpdateIn);
            if (sleepNs > 0) {
                sleepNs = Math.min(sleepNs, 1_000_000L);
                sleepNanos(sleepNs);
            }
            else {
                Thread.yield();
            }
        }
    }

    private static void sleepNanos(long nanos) {
        if (nanos <= 0) {
            return;
        }
        final long ms = nanos / 1_000_000L;
        final int ns = (int) (nanos % 1_000_000L);

        try {
            if (ms > 0) {
                Thread.sleep(ms, ns);
            }
            else {
                Thread.sleep(0, ns);
            }
        } catch (InterruptedException ie) {
            log.error(ie.getLocalizedMessage());
            Thread.currentThread().interrupt();
        }
    }
}
