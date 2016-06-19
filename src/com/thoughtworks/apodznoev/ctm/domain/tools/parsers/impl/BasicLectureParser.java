package com.thoughtworks.apodznoev.ctm.domain.tools.parsers.impl;

import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParseException;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParser;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class BasicLectureParser implements LectureParser {
    public static final String LIGHTNING_KEYWORD = "lightning";
    public static final int LIGHTNING_DURATION_MINUTES = 5;
    public static final String MINUTES_KEYWORD = "min";

    @Override
    public Lecture parse(String line) throws LectureParseException {
        if (line == null) {
            throw new LectureParseException("line is null");
        }
        line = line.trim();

        int indexBetweenTitleDuration = tryFindTitleDurationDelimiter(line);
        String title = line.substring(0, indexBetweenTitleDuration);
        String duration = line.substring(indexBetweenTitleDuration + 1, line.length());
        int durationMinutes = tryParseDuration(duration);
        String speaker = "";//not supported now
        return new Lecture(title, speaker, durationMinutes);
    }

    private int tryParseDuration(String duration) throws LectureParseException {
        //we will avoid using RegExp sacrificing performance for errors readability
        duration = duration.toLowerCase().trim();

        if (duration.equals(LIGHTNING_KEYWORD)) {
            return LIGHTNING_DURATION_MINUTES;
        }

        if (!duration.contains(MINUTES_KEYWORD)) {
            throw new LectureParseException("Cannot find minutes keyword in '" + duration+"'");
        }

        if (!duration.endsWith(MINUTES_KEYWORD)) {
            throw new LectureParseException("Lecture duration should end with '" + MINUTES_KEYWORD + "' keyword");
        }

        String durationMinutesString = duration.substring(0, duration.indexOf(MINUTES_KEYWORD));
        int durationMinutes;
        try {
            durationMinutes = Integer.valueOf(durationMinutesString);
        } catch (NumberFormatException e) {
            throw new LectureParseException(
                    "Cannot parse minutes duration to integer value:" + durationMinutesString
            );
        }

        if (durationMinutes < 1) {
            throw new LectureParseException("Duration cannot be negative or zero, but is:" + durationMinutes);
        }

        return durationMinutes;
    }

    private int tryFindTitleDurationDelimiter(String line) throws LectureParseException {
        int indexBetweenTitleDuration = line.lastIndexOf(' ');
        if (indexBetweenTitleDuration == -1 || indexBetweenTitleDuration == line.length() - 1) {
            throw new LectureParseException("Cannot split lecture to title and duration");
        }

        if (indexBetweenTitleDuration == 0) {
            throw new LectureParseException("Lecture without title");
        }

        return indexBetweenTitleDuration;
    }
}
