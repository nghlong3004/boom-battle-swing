package io.nghlong3004.game.main;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameWindow {

    private final GameFrame gameFrame;
    private final Thread thread;

    public void open() {
        gameFrame.setVisible(true);
        thread.start();
        close();
    }

    public void close() {
        Runtime.getRuntime()
               .addShutdownHook(new Thread(() -> {
                   try {
                       System.out.println("Closing game...");
                       thread.interrupt();
                       thread.join();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
               }));
    }

}
