package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.funutils.ConferenceRandomizer;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.FilePathStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.ScheduleStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;
import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposeException;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposer;
import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParseException;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class CreateScheduleStep extends AbstractStep {
    private final LectureParser lectureParser;
    private final ConferenceScheduleComposer scheduleComposer;

    private CurrentState stepState = CurrentState.CONFIRM_PARSE;
    private Path lecturesFile;
    private List<Lecture> lastParsedLectures;
    private ConferenceSchedule result;

    public CreateScheduleStep(PrintWriter userPrompter,
                              LectureParser lectureParser,
                              ConferenceScheduleComposer scheduleComposer) {
        super(userPrompter);
        this.lectureParser = lectureParser;
        this.scheduleComposer = scheduleComposer;
    }

    @Override
    public StepType getStepType() {
        return StepType.CREATE_SCHEDULE;
    }

    @Override
    public String getInitialQuestion() {
        return "Do you wish to create a Conference Schedule from file: '"
                + lecturesFile.toString() + "'?";
    }

    @Override
    public boolean isFinished() {
        return result != null;
    }

    @Override
    public StepData getCollectedData() {
        return new ScheduleStepData(result);
    }

    @Override
    public void supplyInitialData(StepData initialData) {
        if (!(initialData instanceof FilePathStepData)) {
            throw new IllegalStateException(
                    "Unexpected initial data, expected to be " + FilePathStepData.class +
                            " but is: " + initialData.getClass()
            );
        }

        this.lecturesFile = ((FilePathStepData) initialData).getFilePath();
    }

    @Override
    public void doStep(String userInput) {
        if (stepState == CurrentState.CONFIRM_PARSE) {
            if (!stepConfirmed(userInput))
                return;

            stepState = CurrentState.PARSE_FILE;
        }

        if (stepState == CurrentState.CONFIRM_ERRORS) {
            if (!stepConfirmed(userInput))
                return;

            stepState = CurrentState.GENERATE_SCHEDULE;
        }

        if (stepState == CurrentState.PARSE_FILE) {
            if (!tryParseLectures())
                return;

            stepState = CurrentState.GENERATE_SCHEDULE;
        }

        List<Lecture> parsedLectures = lastParsedLectures;
        if (!generateSchedule(parsedLectures)) {
            failed = true;
        }
    }

    private boolean generateSchedule(List<Lecture> lectures) {
        try {
            //todo validation of same-title lectures omitted for now
            result = scheduleComposer.composeSchedule(getInfo(), lectures);
        } catch (ConferenceScheduleComposeException e){
            console.println("Cannot compose schedule due to:" + e.getMessage());
            return false;
        }

        return true;
    }

    private ConferenceInfo getInfo() {
        return ConferenceRandomizer.randomConferenceInfo();
    }

    private boolean stepConfirmed(String yesNoAnswer) {
        boolean confirm = parseYesNo(yesNoAnswer);
        if (!confirm) {
            console.println("Aborting step");
            failed = true;
            return false;
        }

        return true;
    }

    private boolean tryParseLectures() {
        try (Stream<String> lecturesLinesStream = getFileStream()) {
            if (lecturesLinesStream == null)
                return false;

            List<Lecture> parsedLectures = new ArrayList<>();
            List<ParseError> failedToParse = new ArrayList<>();
            AtomicLong idx = new AtomicLong(0);
            lecturesLinesStream
                    .filter(s -> !s.isEmpty())
                    .forEach(s -> tryParseLine(s, idx.incrementAndGet(), failedToParse, parsedLectures));
            lastParsedLectures = parsedLectures;

            if (!failedToParse.isEmpty()) {
                console.println("There were following parsing errors:");
                failedToParse.forEach(parseError -> console.println(String.format(
                        "Line: #%d '%s' due to: '%s'",
                        parseError.lineNumber, parseError.originalLine, parseError.error
                )));
                console.println("Do you wish to continue?");
                printYesNoQuestion();
                return false;
            }

            return true;
        }
    }

    private void tryParseLine(String line, long lineIndex, List<ParseError> parseErrors,
                              List<Lecture> parsedLectures) {
        try {
            parsedLectures.add(lectureParser.parse(line));
        } catch (LectureParseException e) {
            parseErrors.add(new ParseError(lineIndex, line, e.getMessage()));
        }
    }

    private Stream<String> getFileStream() {
        try {
            return Files.lines(lecturesFile);
        } catch (IOException e) {
            console.println("Failed to open file for read due to:" + e.getMessage());
            failed = true;
            return null;
        }
    }


    private enum CurrentState {
        CONFIRM_PARSE,
        PARSE_FILE,
        CONFIRM_ERRORS,
        GENERATE_SCHEDULE;
    }

    private static class ParseError {
        private final long lineNumber;
        private final String originalLine;
        private final String error;

        public ParseError(long lineNumber, String originalLine, String error) {
            this.lineNumber = lineNumber;
            this.originalLine = originalLine;
            this.error = error;
        }
    }
}
