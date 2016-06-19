package com.thoughtworks.apodznoev.ctm.domain.tools.export;

import java.io.PrintWriter;

/**
 * @author apodznoev
 * @since 19/06/16
 */
public interface ConsoleObjectWriter<T> {

    void writeObject(T object, PrintWriter console);
}
