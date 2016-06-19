package com.thoughtworks.apodznoev.ctm.domain.tools.parsers;

/**
 * Factory which provides various implementations of parsers
 * to obtain {@link com.thoughtworks.apodznoev.ctm.domain.events.Lecture} instances
 * from text representation.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface LectureParserFactory {

    /**
     * Creates new parser using factory settings.
     *
     * @return new lecture parser
     */
    LectureParser createParser();
}
