package com.thoughtworks.apodznoev.ctm.console.interactive.funutils;

import com.thoughtworks.apodznoev.ctm.domain.ConferenceInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;

/**
 * Just to make it not so boring.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public final class ConferenceRandomizer {
    private static final List<String> BUZZ_WORDS = Arrays.asList(
            "effective", "expert", "ultimate", "architecture", "strategic",
            "leading", "innovative", "quantum", "modern", "best"
    );
    private static final List<String> SUBJECTS = Arrays.asList(
            "Java", "supply chain", "coding", "principles",
            "patterns", "Java", "OpenSource", "C++", "practices",
            "FinTech", "banking", "DevOps", "hiring",
            "how to", "wearable", "tech", "security"
    );
    private static final List<String> PLACES = Arrays.asList(
            "Berlin", "London", "Sydney", "San Francisco", "Chicago",
            "Seattle", "Singapore", "Paris", "Munich"
    );
    private static final List<String> TYPES = Arrays.asList(
            "Summit", "Symposium", "Forum", "Conf", "Congress"
    );
    private static final List<String> ADJECTIVES = Arrays.asList(
            "grand", "open", "internal", "outer", "shuddering",
            "private", "public", "local", "upper", "side"
    );
    private static final List<String> ROOMS = Arrays.asList(
            "Room", "Hall", "Basement", "Court", "Roof",
            "Swimming Pool", "Quarters", "building"
    );
    private static final Random rand = new Random(System.currentTimeMillis());


    public static String randomRoom() {
        return rand(PLACES) +
                " " +
                rand(ADJECTIVES) +
                " " +
                rand(ROOMS);
    }

    public static ConferenceInfo randomConferenceInfo() {
        LocalDate time = getAnyTime();
        String title = getAnyTitle();
        return new ConferenceInfo(
                title + " " + time.get(ChronoField.YEAR), time
        );
    }

    private static String getAnyTitle() {
        return rand(BUZZ_WORDS) +
                " " +
                rand(SUBJECTS) +
                " " +
                rand(BUZZ_WORDS) +
                " " +
                rand(SUBJECTS) +
                " " +
                rand(TYPES) +
                " " +
                rand(PLACES);
    }

    private static String rand(List<String> words) {
        return words.get(rand.nextInt(words.size()));
    }

    private static LocalDate getAnyTime() {
        LocalDate now = LocalDate.now();
        int randomDays = rand.nextInt(365 * 5);
        return now.plusDays(randomDays);
    }
}
