package com.thoughtworks.apodznoev.ctm.domain.builders.impl;

import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposer;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposerFactory;

/**
 * Factory implementation which can be configured using {@link ConferenceScheduleComposingOptions}
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class BasicConferenceScheduleFactory implements ConferenceScheduleComposerFactory {
    private volatile ConferenceScheduleComposingOptions composingOptions
            = ConferenceScheduleComposingOptions.basicOptions();

    @Override
    public ConferenceScheduleComposer createComposer() {
        return new SingleDayConferenceScheduleComposer(composingOptions);
    }

    /**
     * Sets options for schedule generation. After invoking of given method,
     * all following created composers will be acting according to options
     */
    public BasicConferenceScheduleFactory setComposingOptions(
            ConferenceScheduleComposingOptions composingOptions) {
        this.composingOptions = composingOptions;
        return this;
    }
}
