package com.thoughtworks.apodznoev.ctm.domain.tools.export.impl;

import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.FileObjectWriter;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.FileObjectWritersFactory;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.WriteOptions;

/**
 * Basic factory implementation of file writers factory which can be customized
 * using {@link WriteOptions}
 *
 * @author apodznoev
 * @since 19/06/16
 */
public class BasicFileWritersFactory implements FileObjectWritersFactory {
    private volatile WriteOptions writeOptions = WriteOptions.defaultOptions();

    @SuppressWarnings("unchecked")
    @Override
    public <T> FileObjectWriter<T> createFileWriter(Class<T> clazz) {
        if (clazz == ConferenceSchedule.class) {
            return (FileObjectWriter<T>) new ScheduleFileWriter(
                    writeOptions.getDateFormatter(), writeOptions.getDayTimeFormatter()
            );
        }

        throw new IllegalArgumentException("Implement me for class:" + clazz);
    }

    public BasicFileWritersFactory setWriteOptions(WriteOptions writeOptions) {
        this.writeOptions = writeOptions;
        return this;
    }
}
