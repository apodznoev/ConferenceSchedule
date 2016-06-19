package com.thoughtworks.apodznoev.ctm.domain.tools.parsers;

import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;

/**
 * Interface which is responsible for providing new {@link Lecture} instances
 * from given text representation.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface LectureParser {

    /**
     * Creates new {@link Lecture} instance from given string
     *
     * @param line not-null line to be transformed to object
     * @return new instance of lecture
     * @throws LectureParseException in case if line is not parseable according to
     *                               concrete parser implementation
     */
    Lecture parse(String line) throws LectureParseException;
}
