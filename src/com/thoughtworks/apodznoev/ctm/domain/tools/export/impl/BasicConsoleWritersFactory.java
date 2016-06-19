package com.thoughtworks.apodznoev.ctm.domain.tools.export.impl;

import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.ConsoleObjectWriter;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.ConsoleObjectWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.WriteOptions;

/**
 * Basic factory implementation of console writers factory which can be customized
 * using {@link WriteOptions}
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class BasicConsoleWritersFactory implements ConsoleObjectWritersFactory {
    private volatile WriteOptions writeOptions = WriteOptions.defaultOptions();

    @Override
    public <T> ConsoleObjectWriter<T> createConsoleWriter(Class<T> clazz) {
        if (clazz == ConferenceSchedule.class) {
            //noinspection unchecked
            return (ConsoleObjectWriter<T>)
                    new ScheduleConsoleWriter(writeOptions.getDateFormatter(), writeOptions.getDayTimeFormatter());
        }

        throw new IllegalArgumentException("Implement me for class:" + clazz);
    }

    public BasicConsoleWritersFactory setWriteOptions(WriteOptions writeOptions) {
        this.writeOptions = writeOptions;
        return this;
    }
}
