package com.thoughtworks.apodznoev.ctm.domain;

import java.time.LocalDate;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class ConferenceInfo {
    private final String conferenceName;
    private final LocalDate conferenceStartDay;

    public ConferenceInfo(String conferenceName, LocalDate conferenceStartDay) {
        this.conferenceName = conferenceName;
        this.conferenceStartDay = conferenceStartDay;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public LocalDate getConferenceStartDay() {
        return conferenceStartDay;
    }
}
