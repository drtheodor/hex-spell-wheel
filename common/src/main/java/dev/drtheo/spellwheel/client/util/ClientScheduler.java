package dev.drtheo.spellwheel.client.util;

import dev.architectury.event.events.client.ClientTickEvent;

import java.util.Deque;
import java.util.LinkedList;

public class ClientScheduler {

    private static final Deque<Task> QUEUE = new LinkedList<>();

    public static void init() {
        ClientTickEvent.CLIENT_POST.register(instance -> tick());
    }

    public static void add(int ticks, Runnable runnable) {
        QUEUE.add(new Task(ticks, runnable));
    }

    private static void tick() {
        QUEUE.removeIf(task -> {
            if (task.ticks > 0)
                return false;

            task.runnable.run();
            return true;
        });
    }

    static class Task {
        int ticks;
        Runnable runnable;

        public Task(int ticks, Runnable runnable) {
            this.ticks = ticks;
            this.runnable = runnable;
        }
    }
}
