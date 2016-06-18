package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import java.io.PrintWriter;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public abstract class AbstractStep implements InteractiveStep {
    protected final PrintWriter console;

    protected AbstractStep(PrintWriter console) {
        this.console = console;
    }
}
