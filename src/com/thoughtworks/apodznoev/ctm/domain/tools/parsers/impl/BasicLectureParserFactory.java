package com.thoughtworks.apodznoev.ctm.domain.tools.parsers.impl;

import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParser;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParserFactory;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class BasicLectureParserFactory implements LectureParserFactory {
    @Override
    public LectureParser createParser() {
        return new BasicLectureParser();
    }
}
