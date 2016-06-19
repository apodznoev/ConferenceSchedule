package com.thoughtworks.apodznoev.ctm.domain.builders;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;
import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;

import java.util.List;

/**
 * todo
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConferenceScheduleComposer {

    ConferenceSchedule composeSchedule(ConferenceInfo conferenceInfo,
                                       List<Lecture> lectures) throws ConferenceScheduleComposeException;
}
