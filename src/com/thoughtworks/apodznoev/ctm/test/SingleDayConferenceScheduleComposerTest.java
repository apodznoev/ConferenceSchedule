package com.thoughtworks.apodznoev.ctm.test;

import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposeException;
import com.thoughtworks.apodznoev.ctm.domain.builders.impl.ConferenceScheduleComposingOptions;
import com.thoughtworks.apodznoev.ctm.domain.builders.impl.SingleDayConferenceScheduleComposer;
import com.thoughtworks.apodznoev.ctm.domain.events.Event;
import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.events.Lunch;
import com.thoughtworks.apodznoev.ctm.domain.events.Networking;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.TrackSchedule;
import org.junit.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class SingleDayConferenceScheduleComposerTest {
    private SingleDayConferenceScheduleComposer composer;

    @Test
    public void testNoBreaksNoNetworking() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder());
        ConferenceSchedule schedule = makeSchedule(10, 20, 30);

        assertEquals(1, schedule.getDaySchedules().size());
        assertEquals(1, schedule.getDaySchedules().get(0).getTracks().size());
        TrackSchedule trackSchedule = schedule.getDaySchedules().get(0).getTracks().get(0);

        checkSchedule(trackSchedule,
                new Check(30, "30#2", LocalTime.of(0, 0)),
                new Check(20, "20#1", LocalTime.of(0, 30)),
                new Check(10, "10#0", LocalTime.of(0, 50))
        );
    }

    @Test
    public void testNoBreaksNetworking() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .addFinalEvent(LocalTime.of(1, 0), LocalTime.of(2, 0),
                        new Networking()));
        ConferenceSchedule schedule = makeSchedule(10, 30, 20, 10, 30);

        assertEquals(1, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(30, "30#1", LocalTime.of(0, 0)),
                new Check(30, "30#4", LocalTime.of(0, 30)),
                new Check(20, "20#2", LocalTime.of(1, 0)),
                new Check(10, "10#0", LocalTime.of(1, 20)),
                new Check(10, "10#3", LocalTime.of(1, 30)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(1, 40))
        );
    }

    @Test
    public void testNoBreaksNetworkingCustomStart() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .setConferenceStart(LocalTime.of(9, 0))
                .addFinalEvent(LocalTime.of(14, 0), LocalTime.of(15, 0),
                        new Networking()));
        ConferenceSchedule schedule = makeSchedule(5, 95, 35, 11, 22, 37);

        assertEquals(1, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(95, "95#1", LocalTime.of(9, 0)),
                new Check(37, "37#5", LocalTime.of(10, 35)),
                new Check(35, "35#2", LocalTime.of(11, 12)),
                new Check(22, "22#4", LocalTime.of(11, 47)),
                new Check(11, "11#3", LocalTime.of(12, 9)),
                new Check(5, "5#0", LocalTime.of(12, 20)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(14, 0))
        );
    }

    @Test
    public void testNoBreaksTwoTracks() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .setConferenceStart(LocalTime.of(7, 30))
                .addFinalEvent(LocalTime.of(10, 0), LocalTime.of(10, 30),
                        new Networking()));
        ConferenceSchedule schedule = makeSchedule(10, 60, 49, 95, 25);

        assertEquals(2, schedule.getDaySchedules().get(0).getTracks().size());
        assertEquals(2, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(95, "95#3", LocalTime.of(7, 30)),
                new Check(60, "60#1", LocalTime.of(9, 5)),
                new Check(25, "25#4", LocalTime.of(10, 5)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(10, 30))
        );

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(1),
                new Check(49, "49#2", LocalTime.of(7, 30)),
                new Check(10, "10#0", LocalTime.of(8, 19)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(10, 0))
        );
    }

    @Test
    public void testOneBreak() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .setConferenceStart(LocalTime.of(11, 10))
                .addBreak(LocalTime.of(14, 0), new Lunch(30))
                .addFinalEvent(LocalTime.of(16, 0), LocalTime.of(16, 30), new Networking())
        );
        ConferenceSchedule schedule = makeSchedule(95, 60, 30, 5, 60, 90);
        assertEquals(2, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(95, "95#0", LocalTime.of(11, 10)),
                new Check(60, "60#1", LocalTime.of(12, 45)),
                new Check(5, "5#3", LocalTime.of(13, 45)),
                new Check(30, "Lunch", LocalTime.of(14, 0)),
                new Check(90, "90#5", LocalTime.of(14, 30)),
                new Check(30, "30#2", LocalTime.of(16, 0)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(16, 30))
        );

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(1),
                new Check(60, "60#4", LocalTime.of(11, 10)),
                new Check(30, "Lunch", LocalTime.of(14, 0)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(16, 0))
        );
    }

    @Test
    public void testTwoBreaks() throws ConferenceScheduleComposeException {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .setConferenceStart(LocalTime.of(9, 0))
                .addBreak(LocalTime.of(12, 0), new Lunch(30))
                .addBreak(LocalTime.of(14, 0), new Lunch(45))
                .addFinalEvent(LocalTime.of(16, 0), LocalTime.of(17, 0), new Networking())
        );
        ConferenceSchedule schedule = makeSchedule(120, 75, 60, 5, 30, 60, 90);
        assertEquals(2, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(120, "120#0", LocalTime.of(9, 0)),
                new Check(60, "60#2", LocalTime.of(11, 0)),
                new Check(30, "Lunch", LocalTime.of(12, 0)),
                new Check(90, "90#6", LocalTime.of(12, 30)),
                new Check(45, "Lunch", LocalTime.of(14, 0)),
                new Check(75, "75#1", LocalTime.of(14, 45)),
                new Check(60, "60#5", LocalTime.of(16, 00)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(17, 0))
        );

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(1),
                new Check(30, "30#4", LocalTime.of(9, 0)),
                new Check(30, "Lunch", LocalTime.of(12, 0)),
                new Check(5, "5#3", LocalTime.of(12, 30)),
                new Check(45, "Lunch", LocalTime.of(14, 0)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(16, 0))
        );
    }

    @Test
    public void testCloseToSample() throws Exception {
        initComposer(new ConferenceScheduleComposingOptions.Builder()
                .setConferenceStart(LocalTime.of(9, 0))
                .addBreak(LocalTime.of(12, 0), new Lunch(60))
                .addFinalEvent(LocalTime.of(16, 0), LocalTime.of(17, 0), new Networking())
        );
        ConferenceSchedule schedule = makeSchedule(
                60, 45, 30, 45, 45, 5, 60, 45, 30, 30, 45, 60, 60
        );
        assertEquals(2, schedule.getDaySchedules().get(0).getTracks().size());

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(0),
                new Check(60, "60#0", LocalTime.of(9, 0)),
                new Check(60, "60#11", LocalTime.of(10, 0)),
                new Check(45, "45#1", LocalTime.of(11, 0)),
                new Check(5, "5#5", LocalTime.of(11, 45)),
                new Check(60, "Lunch", LocalTime.of(12, 0)),
                new Check(60, "60#6", LocalTime.of(13, 0)),
                new Check(60, "60#12", LocalTime.of(14, 0)),
                new Check(45, "45#3", LocalTime.of(15, 0)),
                new Check(45, "45#4", LocalTime.of(15, 45)),
                new Check(30, "30#2", LocalTime.of(16, 30)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(17, 0))
        );

        checkSchedule(schedule.getDaySchedules().get(0).getTracks().get(1),
                new Check(45, "45#7", LocalTime.of(9, 0)),
                new Check(30, "30#8", LocalTime.of(9, 45)),
                new Check(60, "Lunch", LocalTime.of(12, 0)),
                new Check(45, "45#10", LocalTime.of(13, 0)),
                new Check(30, "30#9", LocalTime.of(13, 45)),
                new Check(Event.UNBOUNDED_DURATION, "Networking", LocalTime.of(16, 0))
        );

    }

    private void checkSchedule(TrackSchedule firstTrackSchedule, Check... checks) {
        assertNotNull(firstTrackSchedule);
        NavigableMap<LocalTime, Event> events = firstTrackSchedule.getEventsSchedule();
        assertEquals(checks.length, events.size());
        for (Check check : checks) {
            assertEquals(check.duration, events.get(check.time).getDurationMinutes());
            assertEquals(check.title, events.get(check.time).getTitle());
        }
    }

    private ConferenceSchedule makeSchedule(int... durations) throws ConferenceScheduleComposeException {
        return composer.composeSchedule(null, getLectures(durations));
    }

    private List<Lecture> getLectures(int... durations) {
        List<Lecture> lectures = new ArrayList<>(durations.length);
        int counter = 0;
        for (int duration : durations) {
            lectures.add(new Lecture(String.valueOf(duration) + "#" + counter++, null, duration));
        }
        return lectures;
    }

    private void initComposer(ConferenceScheduleComposingOptions.Builder options) {
        composer = new SingleDayConferenceScheduleComposer(
                options.build()
        );
    }

    private class Check {
        private final int duration;
        private final String title;
        private final LocalTime time;

        private Check(int duration, String title, LocalTime time) {
            this.duration = duration;
            this.title = title;
            this.time = time;
        }
    }
}
