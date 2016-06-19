package com.thoughtworks.apodznoev.ctm.domain.schedules;

import com.thoughtworks.apodznoev.ctm.domain.events.Event;

import java.time.LocalTime;
import java.util.*;

/**
 * Schedules of given track. Contains as general information, like
 * track location or track opening time, as a schedule for every event
 * taking place on given track.
 * Usually track schedule contains from list of {@link com.thoughtworks.apodznoev.ctm.domain.events.Lecture}
 * with several {@link com.thoughtworks.apodznoev.ctm.domain.events.Break} between them
 * and {@link com.thoughtworks.apodznoev.ctm.domain.events.ClosingEvent} at end.
 *
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

    /**
     * Gets unmodifiable view of events taking place on this track.
     * Given map is ordered in ASK order, where keys are - day time of
     * event, which placed as value.
     * Events in given map cannot intersect, i.e. its guarantied that
     * every next map entry will have day time {@code > previousEntryDayTime + previousEntry.getDuration()}
     *
     * @return unmodifiable view of map where key is event start time, value - event by itself.
     */
    public NavigableMap<LocalTime, Event> getEventsSchedule() {
        return Collections.unmodifiableNavigableMap(eventsSchedule);
    }

    /**
     * Gets readable representation of track location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets day time of track opening. Usually it's the same as
     * first event start.
     */
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
