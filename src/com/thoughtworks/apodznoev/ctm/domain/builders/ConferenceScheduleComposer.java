package com.thoughtworks.apodznoev.ctm.domain.builders;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;
import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;

import java.util.List;

/**
 * Interface responsible for composing conference schedule from
 * the given list of lectures.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConferenceScheduleComposer {

    /**
     * Creates conference schedule from given lectures.
     *
     * @param conferenceInfo meta info of conference
     * @param lectures       list of lectures
     * @return new instance of schedule with given lectures.
     * @throws ConferenceScheduleComposeException in case if there are some
     *                                            contradictions with schedule bulding parameters
     *                                            during schedule composing.
     */
    ConferenceSchedule composeSchedule(ConferenceInfo conferenceInfo,
                                       List<Lecture> lectures) throws ConferenceScheduleComposeException;
}
