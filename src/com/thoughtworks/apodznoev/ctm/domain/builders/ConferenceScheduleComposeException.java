package com.thoughtworks.apodznoev.ctm.domain.builders;

/**
 * Exception which can be raised during schedule composing
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class ConferenceScheduleComposeException extends Exception {
    public ConferenceScheduleComposeException(String s) {
        super("Cannot compose conference schedule due to: '" + s + "'");
    }
}
