package com.thoughtworks.apodznoev.ctm.domain.schedules;

import java.util.List;

/**
 * Class which represents schedule for concrete day of conference.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class DaySchedule {
    private final List<TrackSchedule> tracks;

    public DaySchedule(List<TrackSchedule> tracks) {
        this.tracks = tracks;
    }

    /**
     * Gets schedules for every track present on given conference
     *
     * @return list of schedules for each track on given day
     */
    public List<TrackSchedule> getTracks() {
        return tracks;
    }
}
