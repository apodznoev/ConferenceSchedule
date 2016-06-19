package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class Networking implements ClosingEvent {
    @Override
    public String getTitle() {
        return "Networking Event";
    }

    @Override
    public int getDurationMinutes() {
        return UNBOUNDED_DURATION;
    }

    @Override
    public String toString() {
        return "Networking{}";
    }
}
