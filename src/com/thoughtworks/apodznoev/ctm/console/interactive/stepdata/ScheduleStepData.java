package com.thoughtworks.apodznoev.ctm.console.interactive.stepdata;

import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;

/**
 * Step data provided from steps which produced schedule and for
 * steps which manipulate with schedule.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class ScheduleStepData implements StepData {
    private final ConferenceSchedule schedule;

    public ScheduleStepData(ConferenceSchedule schedule) {
        this.schedule = schedule;
    }

    public ConferenceSchedule getSchedule() {
        return schedule;
    }
}
