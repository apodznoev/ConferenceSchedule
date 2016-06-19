package com.thoughtworks.apodznoev.ctm.domain.tools.export;

import java.time.format.DateTimeFormatter;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public class WriteOptions {
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter dayTimeFormatter;

    public WriteOptions(DateTimeFormatter dateTimeFormatter,
                        DateTimeFormatter dayTimeFormatter) {
        this.dateFormatter = dateTimeFormatter;
        this.dayTimeFormatter = dayTimeFormatter;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }

    public DateTimeFormatter getDayTimeFormatter() {
        return dayTimeFormatter;
    }

    public static WriteOptions defaultOptions() {
        return new Builder().build();
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
            return new WriteOptions(dateFormatter,dayTimeFormatter);
        }
    }
}
