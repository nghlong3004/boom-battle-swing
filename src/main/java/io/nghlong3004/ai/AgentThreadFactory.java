package io.nghlong3004.ai;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AgentThreadFactory implements ThreadFactory {
    private final AtomicInteger idx = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "Agent-" + idx.getAndIncrement());
        t.setDaemon(true);
        return t;
    }
}
