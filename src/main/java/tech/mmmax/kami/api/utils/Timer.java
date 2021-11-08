/*
 * Decompiled with CFR 0.151.
 */
package tech.mmmax.kami.api.utils;

public class Timer {
    long startTime = System.currentTimeMillis();
    long delay = 0L;
    boolean paused = false;

    public boolean isPassed() {
        return !this.paused && System.currentTimeMillis() - this.startTime >= this.delay;
    }

    public void resetDelay() {
        this.startTime = System.currentTimeMillis();
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public long getStartTime() {
        return this.startTime;
    }
}

