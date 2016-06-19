package com.thoughtworks.apodznoev.ctm.domain.schedules;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * Main class for this application, holding schedule of conference.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class ConferenceSchedule {
    private final ConferenceInfo conferenceInfo;
    private final List<DaySchedule> daySchedules;

    public ConferenceSchedule(ConferenceInfo conferenceInfo,
                              List<DaySchedule> daySchedules) {
        this.conferenceInfo = conferenceInfo;
        this.daySchedules = daySchedules;
    }

    /**
     * Gets meta info of conference
     *
     * @return conference meta info
     */
    public ConferenceInfo getConferenceInfo() {
        return conferenceInfo;
    }

    /**
     * Gets per-day schedule for given conference
     *
     * @return ordered by day of occurrence every day schedules
     */
    public List<DaySchedule> getDaySchedules() {
        return daySchedules;
    }
}
