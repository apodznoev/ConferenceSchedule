package com.thoughtworks.apodznoev.ctm.domain.builders;

/**
 * Factory which provides new instances of conference schedule composers.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConferenceScheduleComposerFactory {

    /**
     * Provides instance of conference schedule composer based
     * on factory settings.
     *
     * @return some instance of conference schedule composer
     */
    ConferenceScheduleComposer createComposer();
}
