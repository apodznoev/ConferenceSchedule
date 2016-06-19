package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposerFactory;
import com.thoughtworks.apodznoev.ctm.domain.builders.impl.BasicConferenceScheduleFactory;
import com.thoughtworks.apodznoev.ctm.domain.builders.impl.ConferenceScheduleComposingOptions;
import com.thoughtworks.apodznoev.ctm.domain.events.Lunch;
import com.thoughtworks.apodznoev.ctm.domain.events.Networking;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.ConsoleObjectWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.FileObjectWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.WriteOptions;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.impl.BasicConsoleWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.impl.BasicFileWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.LectureParserFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.impl.BasicLectureParserFactory;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * todo
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class SingleStepPerTypeFactory implements StepsFactory {
    private final PrintWriter userPrompter;
    private final LectureParserFactory lectureParserFactory;
    private final ConferenceScheduleComposerFactory scheduleComposerFactory;
    private final ConsoleObjectWritersFactory consoleWritersFactory;
    private final FileObjectWritersFactory fileWritersFactory;

    public SingleStepPerTypeFactory(PrintWriter prompter) {
        this.userPrompter = prompter;

        lectureParserFactory = new BasicLectureParserFactory();

        scheduleComposerFactory = new BasicConferenceScheduleFactory()
                .setComposingOptions(new ConferenceScheduleComposingOptions.Builder()
                        .randomize(true)
                        .setConferenceStart(LocalTime.of(9, 0))
                        .addBreak(LocalTime.of(12, 0), new Lunch(60))
                        .addFinalEvent(LocalTime.of(16, 0), LocalTime.of(17, 0), new Networking())
                        .build());

        consoleWritersFactory = new BasicConsoleWritersFactory()
                .setWriteOptions(
                        new WriteOptions.Builder()
                                .setDayTimeFormat(DateTimeFormatter.ofPattern("hh:mma"))
                                .setDateFormat(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
                                .build());

        fileWritersFactory = new BasicFileWritersFactory()
                .setWriteOptions(
                        new WriteOptions.Builder()
                                .setDayTimeFormat(DateTimeFormatter.ofPattern("hh:mma"))
                                .setDateFormat(DateTimeFormatter.ofPattern("dd.MM.YYYY"))
                                .build()
                );
    }

    @Override
    public InteractiveStep createStep(StepType stepType) {
        switch (stepType) {
            case CREATE_SCHEDULE:
                return new CreateScheduleStep(
                        userPrompter,
                        lectureParserFactory.createParser(),
                        scheduleComposerFactory.createComposer()
                );
            case EDIT_SCHEDULE:
                return new EditScheduleStep(userPrompter);
            case READ_FILE:
                return new ReadFileStep(userPrompter);
            case EXPORT_SCHEDULE:
                return new ExportScheduleStep(userPrompter,
                        consoleWritersFactory.createConsoleWriter(ConferenceSchedule.class),
                        fileWritersFactory.createFileWriter(ConferenceSchedule.class)
                );
            case READ_SCHEDULE:
                return new ReadScheduleStep(userPrompter);
            default:
                throw new IllegalArgumentException("Add implementation for type:" + stepType);
        }
    }
}
