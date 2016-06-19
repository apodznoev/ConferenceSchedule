package com.thoughtworks.apodznoev.ctm.domain.tools.export;

import java.time.format.DateTimeFormatter;

/**
 * Class-holder of parameters, which customizes writing format for
 * {@link ConsoleObjectWriter} and {@link FileObjectWriter}
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class WriteOptions {
    private static final WriteOptions DEFAULT_OPTIONS = new Builder().build();

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dayTimeFormatter;

    private WriteOptions(DateTimeFormatter dateTimeFormatter,
                         DateTimeFormatter dayTimeFormatter) {
        this.dateFormatter = dateTimeFormatter;
        this.dayTimeFormatter = dayTimeFormatter;
    }

    /**
     * Gets formatter to format date (without hours and minutes) only
     *
     * @return formatter for dates set during construction
     */
    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    /**
     * Gets formatter to format time of day (without days, moths and so on) only
     *
     * @return formatter for time within day, set during construction
     */
    public DateTimeFormatter getDayTimeFormatter() {
        return dayTimeFormatter;
    }

    /**
     * Creates default options, which uses
     * {@link DateTimeFormatter#ISO_DATE} and {@link DateTimeFormatter#ISO_TIME}
     *
     * @return singleton instance of write options
     */
    public static WriteOptions defaultOptions() {
        return DEFAULT_OPTIONS;
    }

    public static class Builder {
        private DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE;
        private DateTimeFormatter dayTimeFormatter = DateTimeFormatter.ISO_TIME;

        public Builder setDateFormat(DateTimeFormatter dateFormatter) {
            this.dateFormatter = dateFormatter;
            return this;
        }

        public Builder setDayTimeFormat(DateTimeFormatter dayTimeFormatter) {
            this.dayTimeFormatter = dayTimeFormatter;
            return this;

        }

        public WriteOptions build() {
            return new WriteOptions(dateFormatter, dayTimeFormatter);
        }
    }
}
