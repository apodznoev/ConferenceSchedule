package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public interface Event {
    int UNBOUNDED_DURATION = -1;

    String getTitle();

    int getDurationMinutes();
}
