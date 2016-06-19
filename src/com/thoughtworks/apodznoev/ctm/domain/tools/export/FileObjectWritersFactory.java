package com.thoughtworks.apodznoev.ctm.domain.tools.export;

/**
 * Factory creating new instances of object to file writers with
 * given class type.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface FileObjectWritersFactory {

    /**
     * Creates new instance of writer
     *
     * @param clazz class which instances will be possible to write
     * @return new instance of writer
     */
    <T> FileObjectWriter<T> createFileWriter(Class<T> clazz);
}
