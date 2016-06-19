package com.thoughtworks.apodznoev.ctm.domain.schedules;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;

import java.time.LocalDate;
import java.util.List;

/**
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

    public ConferenceInfo getConferenceInfo() {
        return conferenceInfo;
    }

    public List<DaySchedule> getDaySchedules() {
        return daySchedules;
    }
}
