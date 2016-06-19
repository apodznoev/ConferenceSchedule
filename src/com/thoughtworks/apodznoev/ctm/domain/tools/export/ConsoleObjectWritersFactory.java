package com.thoughtworks.apodznoev.ctm.domain.tools.export;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConsoleObjectWritersFactory {

    <T> ConsoleObjectWriter<T> createConsoleWriter(Class<T> clazz);
}
