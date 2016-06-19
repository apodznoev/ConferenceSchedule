package com.thoughtworks.apodznoev.ctm.domain.tools.export;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public interface FileObjectWritersFactory {

    <T> FileObjectWriter<T> createFileWriter(Class<T> clazz);
}
