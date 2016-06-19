package com.thoughtworks.apodznoev.ctm.domain.events;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class Lecture implements Event {
    private final String title;
    private final String speaker;
    private final int durationMinutes;

    public Lecture(String title, String speaker, int durationMinutes) {
        this.title = title;
        this.speaker = speaker;
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getSpeaker(){
        return speaker;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "title='" + title + '\'' +
                ", speaker='" + speaker + '\'' +
                ", duration=" + durationMinutes +
                '}';
    }
}
