package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * Interface for all closing events - last on track schedule.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface ClosingEvent extends Event {

    @Override
    default int getDurationMinutes() {
        return UNBOUNDED_DURATION;
    }
}
