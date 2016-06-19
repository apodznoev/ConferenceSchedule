package com.thoughtworks.apodznoev.ctm.domain.builders.impl;

import com.thoughtworks.apodznoev.ctm.domain.events.Break;
import com.thoughtworks.apodznoev.ctm.domain.events.ClosingEvent;
import com.thoughtworks.apodznoev.ctm.domain.events.Lunch;
import com.thoughtworks.apodznoev.ctm.domain.events.Networking;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class-holder for options of the conference schedule to be composed.
 * Determines amount of breaks, presence of closing event, schedule start
 * and other stuff.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class ConferenceScheduleComposingOptions {
    private static final ConferenceScheduleComposingOptions BASIC_OPTIONS =
            new Builder()
                    .randomize(false)
                    .setConferenceStart(LocalTime.of(9, 0))
                    .addBreak(LocalTime.of(12, 0), new Lunch(60))
                    .addFinalEvent(LocalTime.of(16, 0), LocalTime.of(17, 0), new Networking())
                    .build();

    private final boolean randomize;
    private final LocalTime conferenceStart;
    private final List<BreakOption> breaks;
    private final FinalEventOption finalEvent;

    private ConferenceScheduleComposingOptions(boolean randomize,
                                               LocalTime conferenceStart,
                                               List<BreakOption> breaks,
                                               FinalEventOption finalEvent) {
        validateOptions(conferenceStart, breaks, finalEvent);
        this.randomize = randomize;
        this.conferenceStart = conferenceStart;
        this.breaks = breaks;
        this.finalEvent = finalEvent;
    }

    /**
     * Indicates if events can be shuffled after schedule composing.
     */
    public boolean isRandomize() {
        return randomize;
    }

    public LocalTime getConferenceStart() {
        return conferenceStart;
    }

    private void validateOptions(LocalTime conferenceStart,
                                 List<BreakOption> breaks,
                                 FinalEventOption finalEvent) {
        validateBreaks(conferenceStart, breaks);
        validateClosingEvent(conferenceStart, breaks, finalEvent);
    }

    private void validateBreaks(LocalTime conferenceStart, List<BreakOption> breaks) {
        if (breaks.isEmpty())
            return;

        LocalTime previousBreakEndTime = conferenceStart;

        for (BreakOption breakOption : breaks) {
            if (previousBreakEndTime.isAfter(breakOption.start)) {
                throw new IllegalStateException("Next break cannot start before previous ended, but break:" +
                        breakOption.event + " starts before:" + previousBreakEndTime);
            }
            previousBreakEndTime = previousBreakEndTime.plusMinutes(breakOption.event.getDurationMinutes());
        }
    }

    private void validateClosingEvent(LocalTime conferenceStart,
                                      List<BreakOption> breaks,
                                      FinalEventOption finalEvent) {
        if (finalEvent == null) {
            return;
        }

        if (conferenceStart.isAfter(finalEvent.floatingStartMin)) {
            //todo overnight parties not supported yet
            throw new IllegalStateException("Final event cannot start before the conference");
        }

        if (breaks.isEmpty())
            return;

        BreakOption lastBreak = breaks.get(breaks.size() - 1);
        LocalTime lastBreakEnd = lastBreak.start.plus(
                Duration.ofMinutes(lastBreak.event.getDurationMinutes())
        );

        if (lastBreakEnd.isAfter(finalEvent.floatingStartMax)) {
            throw new IllegalStateException("Last break must be finished before final event started");
        }

    }

    /**
     * Gets basic options for schedule generation - with one {@link Lunch},
     * ending {@link Networking} and not very early start.
     */
    public static ConferenceScheduleComposingOptions basicOptions() {
        return BASIC_OPTIONS;
    }

    /**
     * Gets amount of breaks during single track
     *
     * @return non-negative integer
     */
    public int getBreaksCount() {
        return breaks.size();
    }

    /**
     * Gets break for given index
     */
    public Break getBreak(int i) {
        return breaks.get(i).event;
    }

    /**
     * Gets day time when break with given index starts
     */
    public LocalTime getBreakStart(int i) {
        return breaks.get(i).start;
    }

    /**
     * Gets the minimal time, when closing event can take place.
     * In case if closing event is not provided - returns latest available time.
     * Just for the sake of generation simplicity right now.
     */
    public LocalTime getFinalEventMinStart() {
        return finalEvent == null ? LocalTime.of(23, 59) : finalEvent.floatingStartMin;
    }

    /**
     * Gets the latest time, when closing event will take place.
     * In case if closing event is not provided - returns latest available time.
     * Just for the sake of generation simplicity also.
     */
    public LocalTime getFinalEventLastStart() {
        return finalEvent == null ? LocalTime.of(23, 59) : finalEvent.floatingStartMax;
    }

    /**
     * Gets all configured breaks
     */
    public List<Break> getBreaks() {
        return breaks.stream().map(breakOption -> breakOption.event).collect(Collectors.toList());
    }

    /**
     * Gets closing event if provided.
     */
    public ClosingEvent getFinalEvent() {
        return finalEvent == null ? null : finalEvent.event;
    }

    public static class Builder {
        private boolean randomize;
        private LocalTime conferenceStart = LocalTime.of(0, 0);
        private List<BreakOption> breaks = new ArrayList<>();
        private FinalEventOption finalEventOption;

        /**
         * In case of {@code true } events will be shuffled after schedule composing.
         */
        public Builder randomize(boolean randomize) {
            this.randomize = randomize;
            return this;
        }

        public Builder setConferenceStart(LocalTime conferenceStart) {
            this.conferenceStart = conferenceStart;
            return this;
        }

        /**
         * Adds break at given time
         *
         * @param breakTime       time of break
         * @param conferenceBreak break event
         */
        public Builder addBreak(LocalTime breakTime, Break conferenceBreak) {
            breaks.add(new BreakOption(breakTime, conferenceBreak));
            return this;
        }

        /**
         * Adds final event which will be placed in given interval
         *
         * @param floatingStartMin minimal time, when closing event can take place
         * @param floatingStartMax maximal time, before which (inclusive) event will take place
         * @param event closing event
         */
        public Builder addFinalEvent(LocalTime floatingStartMin,
                                     LocalTime floatingStartMax,
                                     ClosingEvent event) {
            if (floatingStartMin.isAfter(floatingStartMax)) {
                throw new IllegalArgumentException("End point of " +
                        "event start interval cannot be before its start point");
            }

            this.finalEventOption = new FinalEventOption(
                    floatingStartMin, floatingStartMax, event
            );
            return this;
        }

        public ConferenceScheduleComposingOptions build() {
            Collections.sort(breaks);
            return new ConferenceScheduleComposingOptions(
                    randomize, conferenceStart, new ArrayList<>(breaks),
                    finalEventOption
            );
        }
    }

    private static class FinalEventOption {
        private final LocalTime floatingStartMin;
        private final LocalTime floatingStartMax;
        private final ClosingEvent event;

        private FinalEventOption(LocalTime floatingStartMin,
                                 LocalTime floatingStartMax,
                                 ClosingEvent event) {
            this.floatingStartMin = floatingStartMin;
            this.floatingStartMax = floatingStartMax;
            this.event = event;
        }
    }

    private static class BreakOption implements Comparable<BreakOption> {
        private final LocalTime start;
        private final Break event;

        public BreakOption(LocalTime start, Break event) {
            this.start = start;
            this.event = event;
        }

        @Override
        public int compareTo(BreakOption o) {
            return this.start.compareTo(o.start);
        }
    }
}
