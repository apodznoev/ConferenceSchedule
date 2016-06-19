package com.thoughtworks.apodznoev.ctm.domain.builders.impl;

import com.thoughtworks.apodznoev.ctm.console.interactive.funutils.ConferenceRandomizer;
import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposeException;
import com.thoughtworks.apodznoev.ctm.domain.builders.ConferenceScheduleComposer;
import com.thoughtworks.apodznoev.ctm.domain.events.Break;
import com.thoughtworks.apodznoev.ctm.domain.events.ClosingEvent;
import com.thoughtworks.apodznoev.ctm.domain.events.Event;
import com.thoughtworks.apodznoev.ctm.domain.events.Lecture;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.DaySchedule;
import com.thoughtworks.apodznoev.ctm.domain.schedules.TrackSchedule;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class SingleDayConferenceScheduleComposer implements ConferenceScheduleComposer {
    private final ConferenceScheduleComposingOptions options;

    public SingleDayConferenceScheduleComposer(ConferenceScheduleComposingOptions composingOptions) {
        this.options = composingOptions;
    }

    @Override
    public ConferenceSchedule composeSchedule(ConferenceInfo conferenceInfo,
                                              List<Lecture> lectures)
            throws ConferenceScheduleComposeException {
        if (lectures.isEmpty()) {
            throw new ConferenceScheduleComposeException("no lectures provided");
        }

        validateDurations(lectures);
        List<TrackSchedule> tracks = composeTracks(new ArrayList<>(lectures));
        DaySchedule daySchedule = new DaySchedule(tracks);
        return new ConferenceSchedule(
                conferenceInfo, Collections.singletonList(daySchedule)
        );
    }

    private void validateDurations(List<Lecture> lectures) throws ConferenceScheduleComposeException {
        int maxDuration = lectures.stream().mapToInt(Lecture::getDurationMinutes).max().getAsInt();
        Lecture tooBigLecture = lectures
                .stream()
                .filter(lecture -> lecture.getDurationMinutes() == maxDuration)
                .findFirst()
                .get();
        int maxAllowedLectureDuration = getMaxAllowedLectureDuration(options);
        if (maxDuration > maxAllowedLectureDuration) {
            throw new ConferenceScheduleComposeException(
                    "not enough time for lecture " + tooBigLecture + " between breaks"
            );
        }
    }

    private int getMaxAllowedLectureDuration(ConferenceScheduleComposingOptions options) {
        int maxAllowedDurationMinutes = 0;
        LocalTime previousBreakEnd = options.getConferenceStart();
        for (int i = 0; i < options.getBreaksCount(); i++) {
            LocalTime newBreakStart = options.getBreakStart(i);
            int durationBetweenBreaks = (int) Duration.between(previousBreakEnd, newBreakStart)
                    .get(ChronoUnit.MINUTES);
            maxAllowedDurationMinutes = Math.max(maxAllowedDurationMinutes, durationBetweenBreaks);

            int breakDuration = options.getBreak(i).getDurationMinutes();
            previousBreakEnd = newBreakStart.plusMinutes(breakDuration);
        }

        maxAllowedDurationMinutes = Math.max(maxAllowedDurationMinutes,
                (int) Duration.between(previousBreakEnd, options.getFinalEventLastStart())
                        .get(ChronoUnit.MINUTES));

        return maxAllowedDurationMinutes;
    }

    private List<TrackSchedule> composeTracks(List<Lecture> lectures) throws ConferenceScheduleComposeException {
        //sort by duration to apply greedy algorithm
        Collections.sort(lectures, (o1, o2) -> Integer.compare(o1.getDurationMinutes(), o2.getDurationMinutes()));

        List<TrackBuilder> trackBuilders = new ArrayList<>();
        TrackBuilder tb;
        trackBuilders.add(tb = new TrackBuilder(options));

        ListIterator<Lecture> it = lectures.listIterator();
        while (true) {
            Lecture lecture = it.hasNext() ? it.next() : null;
            if (lecture == null) {//given track builder tried all available lectures
                if (lectures.isEmpty()) {
                    //all lectures placed to tracks
                    break;
                }

                //track cannot hold any lecture anymore, but there are still not distributed ones
                trackBuilders.add(tb = new TrackBuilder(options));
                resetIter(it);
                continue;
            }

            if (tb.tryAdd(lecture)) {
                //was added to given track
                it.remove();
            }
        }

        if (options.isRandomize())
            trackBuilders.forEach(TrackBuilder::randomize);

        return trackBuilders.stream()
                .map(TrackBuilder::build)
                .collect(Collectors.toList());
    }

    private void resetIter(ListIterator<Lecture> it) {
        while (it.hasPrevious()) {
            it.previous();
        }
    }

    private static class TrackBuilder {
        private final List<Break> breaks;
        private final LocalTime trackStart;
        private final List<Session> sessions;
        private final LocalTime finalEventStartMin;
        private final ClosingEvent finalEvent;
        private int lastFilledSession = -1;

        public TrackBuilder(ConferenceScheduleComposingOptions options) {
            sessions = new ArrayList<>(options.getBreaksCount() + 1);
            LocalTime start = options.getConferenceStart();
            for (int i = 0; i < options.getBreaksCount(); i++) {
                int sessionDuration = (int) Duration.between(start, options.getBreakStart(i))
                        .get(ChronoUnit.MINUTES);
                sessions.add(new Session(sessionDuration));
                start = options.getBreakStart(i).plusMinutes(options.getBreak(i).getDurationMinutes());
            }

            int lastSessionDuration = (int) Duration.between(start, options.getFinalEventLastStart())
                    .get(ChronoUnit.MINUTES);
            sessions.add(new Session(lastSessionDuration));
            trackStart = options.getConferenceStart();
            breaks = options.getBreaks();
            finalEventStartMin = options.getFinalEventMinStart();
            finalEvent = options.getFinalEvent();
        }

        public boolean tryAdd(Lecture lecture) {
            int sessionNumToProbe = lastFilledSession++;
            int attemptsCounter = 0;

            while (true) {
                if (attemptsCounter == sessions.size()) {
                    return false;
                }

                sessionNumToProbe = sessionNumToProbe % sessions.size();

                Session sessionToFill = sessions.get(sessionNumToProbe);
                if (sessionToFill.tryAdd(lecture)) {
                    return true;
                }

                attemptsCounter++;
                sessionNumToProbe++;
            }
        }

        public void randomize() {
            //can safely reorder events within single session
            sessions.stream().forEach(session -> Collections.shuffle(session.lectures));
        }

        public TrackSchedule build() {
            TreeMap<Integer, Event> schedule = new TreeMap<>();
            LocalTime start = trackStart;
            int breakNumber = 0;
            for(Session session : sessions) {
                for(Lecture lecture : session.lectures) {
                    schedule.put(start.get(ChronoField.MINUTE_OF_DAY), lecture);
                    start.plusMinutes(lecture.getDurationMinutes());
                }
                start.plusMinutes(session.remainingMinutes);
                if(breakNumber != breaks.size()) {
                    Break breakBetweenSessions = breaks.get(breakNumber);
                    schedule.put(start.get(ChronoField.MINUTE_OF_DAY), breakBetweenSessions);
                    start.plusMinutes(breakBetweenSessions.getDurationMinutes());
                    breakNumber++;
                }
            }

            int finalEventTime = Math.max(
                    finalEventStartMin.get(ChronoField.MINUTE_OF_DAY), start.get(ChronoField.MINUTE_OF_DAY)
            );
            schedule.put(finalEventTime, finalEvent);

            return new TrackSchedule(
                    ConferenceRandomizer.randomRoom(),
                    trackStart, schedule
            );
        }

        private class Session {
            private final List<Lecture> lectures = new ArrayList<>();
            private int remainingMinutes;

            private Session(int durationMinutes) {
                remainingMinutes = durationMinutes;
            }

            public boolean tryAdd(Lecture lecture) {
                if (lecture.getDurationMinutes() > remainingMinutes) {
                    return false;
                }

                lectures.add(lecture);
                remainingMinutes -= lecture.getDurationMinutes();
                return true;
            }
        }
    }
}
