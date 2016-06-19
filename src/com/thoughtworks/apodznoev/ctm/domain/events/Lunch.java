package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class Lunch implements Break {
    private final int lunchDuration;

    public Lunch(int lunchDuration) {
        this.lunchDuration = lunchDuration;
    }

    @Override
    public String getTitle() {
        return "Lunch";
    }

    @Override
    public int getDurationMinutes() {
        return lunchDuration;
    }

    @Override
    public String toString() {
        return "Lunch{" +
                "lunchDuration=" + lunchDuration +
                '}';
    }
}
