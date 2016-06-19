package com.thoughtworks.apodznoev.ctm.domain.tools.export.impl;

import com.thoughtworks.apodznoev.ctm.domain.events.Break;
import com.thoughtworks.apodznoev.ctm.domain.events.ClosingEvent;
import com.thoughtworks.apodznoev.ctm.domain.events.Event;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.DaySchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.TrackSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.ConsoleObjectWriter;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.WriteOptions;
import com.thoughtworks.apodznoev.ctm.domain.tools.parsers.impl.BasicLectureParser;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class ScheduleConsoleWriter implements ConsoleObjectWriter<ConferenceSchedule> {
    private final DateTimeFormatter dateFormat;
    private final DateTimeFormatter daytimeFormat;

    //that can be replaced with configurable factories in the future
    public ScheduleConsoleWriter(WriteOptions writeOptions) {
        this.dateFormat = writeOptions.getDateFormatter();
        this.daytimeFormat = writeOptions.getDayTimeFormatter();
    }

    @Override
    public void writeObject(ConferenceSchedule schedule, PrintWriter console) {
        console.print(schedule.getConferenceInfo().getConferenceName());
        console.print(" ");
        dateFormat.formatTo(schedule.getConferenceInfo().getConferenceStartDay(), console);
        console.println();
        String indent = "\t";
        int dayCounter = 1;
        for (DaySchedule daySchedule : schedule.getDaySchedules()) {
            console.println(indent + "_______________________________________");
            console.println(indent + "Day " + dayCounter);
            int trackCounter = 1;
            indent = "\t\t";
            for (TrackSchedule trackSchedule : daySchedule.getTracks()) {
                console.print(indent + "Track " + trackCounter);
                console.println(indent + " location: " + trackSchedule.getLocation());
                console.print(indent + "Track opened from: ");
                daytimeFormat.formatTo(trackSchedule.getTrackStart(), console);
                console.println();
                console.println(indent + "Schedule:");
                indent = "\t\t\t";
                for (Map.Entry<LocalTime, Event> entry : trackSchedule.getEventsSchedule().entrySet()) {
                    Event event = entry.getValue();
                    console.print(indent);
                    daytimeFormat.formatTo(entry.getKey(), console);
                    console.print(" ");
                    console.print(event.getTitle());
                    if (!(event instanceof Break) && !(event instanceof ClosingEvent)) {
                        console.print(" ");
                        //todo that could be passed as mapping via FactoryOptions, but later
                        if (event.getDurationMinutes() == BasicLectureParser.LIGHTNING_DURATION_MINUTES) {
                            console.print(BasicLectureParser.LIGHTNING_KEYWORD);
                        } else {
                            console.print(event.getDurationMinutes());
                            console.print(BasicLectureParser.MINUTES_KEYWORD);
                        }
                    }
                    console.println();
                }
                indent = "\t\t";
                trackCounter++;
                console.println();
            }
            indent = "\t";
            dayCounter++;
        }
    }
}
