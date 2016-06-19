package com.thoughtworks.apodznoev.ctm.domain.tools.export;

import java.io.PrintWriter;

/**
 * Interface for writing generic object to console.
 * Actually, any stream can be used, but this is an emphasis
 * that class is intended for console needs only.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConsoleObjectWriter<T> {

    /**
     * Writes object in human-readable form to given stream.
     * Stream should be closed outside.
     *
     * @param object  to write
     * @param console stream connected to human-operated console
     */
    void writeObject(T object, PrintWriter console);
}
