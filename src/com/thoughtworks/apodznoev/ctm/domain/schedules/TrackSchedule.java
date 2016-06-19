package com.thoughtworks.apodznoev.ctm.domain.schedules;

import com.thoughtworks.apodznoev.ctm.domain.events.Event;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class TrackSchedule {
    private final String location;
    private final LocalTime trackStart;
    private final TreeMap<Integer, Event> eventsSchedule;

    public TrackSchedule(String location,
                         LocalTime trackStart,
                         TreeMap<Integer, Event> eventsSchedule) {
        //here we assume that overnight conferences not supported yet
        validateStart(trackStart, eventsSchedule);
        validateIntersections(trackStart, eventsSchedule);

        this.location = location;
        this.trackStart = trackStart;
        this.eventsSchedule = eventsSchedule;
    }

    private void validateStart(LocalTime trackStart, TreeMap<Integer, Event> eventsSchedule) {
        if (trackStart.get(ChronoField.MINUTE_OF_DAY) > eventsSchedule.firstKey()) {
            throw new IllegalArgumentException("Track start should be before first event start");
        }
    }

    private void validateIntersections(LocalTime trackStart, TreeMap<Integer, Event> eventsSchedule) {
        int previousEventEnd = trackStart.get(ChronoField.MINUTE_OF_DAY) +
                eventsSchedule.firstEntry().getValue().getDurationMinutes();

        for (Map.Entry<Integer, Event> entry : eventsSchedule.tailMap(previousEventEnd, false).entrySet()) {
            if (entry.getKey() < previousEventEnd) {
                throw new IllegalArgumentException(
                        "Events cannot intersect! But event: " + entry.getValue() +
                                " starts before previous one ended. " +
                                "All events are:" + eventsSchedule);
            }
            previousEventEnd += entry.getValue().getDurationMinutes();
        }
    }
}
