package com.thoughtworks.apodznoev.ctm.test;

import com.thoughtworks.apodznoev.ctm.console.interactive.InteractiveConsoleProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;

import static org.junit.Assert.assertFalse;

/**
 * @author apodznoev
 * @since 17/06/16
 */
public class InteractiveConsoleProcessorTest {
    private InteractiveConsoleProcessor processor;
    @Before
    public void setUp() throws Exception {
        processor = new InteractiveConsoleProcessor(
                new PrintWriter(System.out), new PrintWriter(System.err)
        );

    }

    @Test
    public void testNotFinishedAtFirstInput() {
        processor.processInput("test");
        assertFalse(processor.finished());
    }

}
