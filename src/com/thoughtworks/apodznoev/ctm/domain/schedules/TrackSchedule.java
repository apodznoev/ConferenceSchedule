package com.thoughtworks.apodznoev.ctm.domain.schedules;

import com.thoughtworks.apodznoev.ctm.domain.events.Event;

import java.time.LocalTime;
import java.util.*;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class TrackSchedule {
    private final String location;
    private final LocalTime trackStart;
    private final NavigableMap<LocalTime, Event> eventsSchedule;

    public TrackSchedule(String location,
                         LocalTime trackStart,
                         NavigableMap<LocalTime, Event> eventsSchedule) {
        //here we assume that overnight conferences not supported yet
        validateStart(trackStart, eventsSchedule);
        validateIntersections(trackStart, eventsSchedule);

        this.location = location;
        this.trackStart = trackStart;
        this.eventsSchedule = eventsSchedule;
    }

    public NavigableMap<LocalTime, Event> getEventsSchedule() {
        return Collections.unmodifiableNavigableMap(eventsSchedule);
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getTrackStart() {
        return trackStart;
    }

    private void validateStart(LocalTime trackStart, NavigableMap<LocalTime, Event> eventsSchedule) {
        if (trackStart.isAfter(eventsSchedule.firstKey())) {
            throw new IllegalArgumentException("Track start should be before first event start");
        }
    }

    private void validateIntersections(LocalTime trackStart, NavigableMap<LocalTime, Event> eventsSchedule) {
        LocalTime previousEventEnd = trackStart.plusMinutes(
                eventsSchedule.firstEntry().getValue().getDurationMinutes());

        for (Map.Entry<LocalTime, Event> entry : eventsSchedule.tailMap(previousEventEnd, false).entrySet()) {
            if (entry.getKey().isBefore(previousEventEnd)) {
                throw new IllegalArgumentException(
                        "Events cannot intersect! But event: " + entry.getValue() +
                                " starts before previous one ended. " +
                                "All events are:" + eventsSchedule);
            }
            previousEventEnd = previousEventEnd.plusMinutes(entry.getValue().getDurationMinutes());
        }
    }
}
