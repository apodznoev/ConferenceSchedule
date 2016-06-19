package com.thoughtworks.apodznoev.ctm.domain.schedules;

import java.util.List;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class DaySchedule {
    private final List<TrackSchedule> tracks;

    public DaySchedule(List<TrackSchedule> tracks) {
        this.tracks = tracks;
    }
}
