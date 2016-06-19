package com.thoughtworks.apodznoev.ctm.domain.tools.parsers;

/**
 * Exception thrown in case if {@link LectureParser#parse(String)} method failed
 * to generate new lecture object
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class LectureParseException extends Exception {
    public LectureParseException(String message) {
        super(message);
    }
}
