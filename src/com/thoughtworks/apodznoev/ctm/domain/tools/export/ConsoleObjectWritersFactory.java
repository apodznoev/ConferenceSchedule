package com.thoughtworks.apodznoev.ctm.domain.tools.export;

/**
 * Factory which creates different console writers with custom parameters.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConsoleObjectWritersFactory {

    /**
     * Create console writer which can write objects of given class to provided
     * console stream.
     *
     * @param clazz which objects to be written
     * @return new instance of console writer
     */
    <T> ConsoleObjectWriter<T> createConsoleWriter(Class<T> clazz);
}
