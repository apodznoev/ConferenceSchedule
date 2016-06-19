package com.thoughtworks.apodznoev.ctm.domain.tools.export.impl;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;
import com.thoughtworks.apodznoev.ctm.domain.events.Break;
import com.thoughtworks.apodznoev.ctm.domain.events.Event;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.DaySchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.TrackSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.FileObjectWriter;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.impl.BasicLectureParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Concrete implementation for file writer which is responsible for writing
 * of {@link ConferenceSchedule} instances in human-readable form.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class ScheduleFileWriter implements FileObjectWriter<ConferenceSchedule> {
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dayTimeFormatter;

    ScheduleFileWriter(DateTimeFormatter dateFormatter, DateTimeFormatter dayTimeFormatter) {
        this.dateFormatter = dateFormatter;
        this.dayTimeFormatter = dayTimeFormatter;
    }

    @Override
    public void writeObject(ConferenceSchedule schedule, Path filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writeConferenceInfo(writer, schedule.getConferenceInfo());
            writer.newLine();
            int day = 1;
            for (DaySchedule daySchedule : schedule.getDaySchedules()) {
                writeDaySchedule(writer, daySchedule, day++);
                writer.newLine();
            }
        }
    }

    private void writeConferenceInfo(BufferedWriter writer, ConferenceInfo conferenceInfo) throws IOException {
        String name = conferenceInfo.getConferenceName();
        LocalDate startDay = conferenceInfo.getConferenceStartDay();
        writer.write("Conference " + name + " ");
        writer.newLine();
        writer.write("Conference will take place at: ");
        dateFormatter.formatTo(startDay, writer);
    }

    private void writeDaySchedule(BufferedWriter writer, DaySchedule daySchedule, int dayNumber) throws IOException {
        writer.write("Day " + dayNumber);
        writer.newLine();
        int track = 1;
        for (TrackSchedule trackSchedule : daySchedule.getTracks()) {
            writeTrackSchedule(writer, trackSchedule, track++);
            writer.newLine();
        }
    }

    private void writeTrackSchedule(BufferedWriter writer,
                                    TrackSchedule trackSchedule, int trackNumber) throws IOException {
        writer.write("Track #" + trackNumber);
        writer.newLine();
        writer.write("Location:" + trackSchedule.getLocation());
        writer.newLine();
        writer.write("Opened at: ");
        dayTimeFormatter.formatTo(trackSchedule.getTrackStart(), writer);
        writer.newLine();
        writer.write("Schedule:");
        writer.newLine();
        for (Map.Entry<LocalTime, Event> entry : trackSchedule.getEventsSchedule().entrySet()) {
            dayTimeFormatter.formatTo(entry.getKey(), writer);
            writer.write(" ");
            writeEvent(writer, entry.getValue());
            writer.newLine();
        }
    }

    private void writeEvent(BufferedWriter writer, Event event) throws IOException {
        String title = event.getTitle();
        int durationMinutes = event.getDurationMinutes();
        writer.write(title);
        if (!skipDuration(event)) {
            writer.write(" ");
            writer.write(formatDuration(durationMinutes));
        }
    }

    private String formatDuration(int durationMinutes) {
        if (durationMinutes == BasicLectureParser.LIGHTNING_DURATION_MINUTES) {
            return BasicLectureParser.LIGHTNING_KEYWORD;
        }
        return durationMinutes + BasicLectureParser.MINUTES_KEYWORD;
    }

    private boolean skipDuration(Event event) {
        return event.getDurationMinutes() == Event.UNBOUNDED_DURATION || event instanceof Break;
    }
}
