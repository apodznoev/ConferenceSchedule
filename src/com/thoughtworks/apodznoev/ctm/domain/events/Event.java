package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * Common interface for all events on conference
 *
 * @author apodznoev
 * @since 18/06/16
 */
public interface Event {
    /**
     * Indicates that event does not have duration.
     * Usually a case for the last event in track schedule.
     */
    int UNBOUNDED_DURATION = -1;

    /**
     * Gets title of the event.
     */
    String getTitle();

    /**
     * Gets duration of the event in minutes.
     *
     * @return positive integer
     */
    int getDurationMinutes();
}
