package com.thoughtworks.apodznoev.ctm.domain.tools.parsers;

import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;

/**
 * todo
 * @author apodznoev
 * @since 19/06/16
 */
public interface LectureParser {

    Lecture parse(String line) throws LectureParseException;
}
