package com.thoughtworks.apodznoev.ctm.domain.tools.export;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public interface FileObjectWriter<T> {

    /**
     * Writes object to file located by given path.
     * File must exists and be writable, otherwise exception will be raised.
     *
     * @param object   object to be written
     * @param filePath path pointing to file destination
     */
    void writeObject(T object, Path filePath) throws IOException;
}
