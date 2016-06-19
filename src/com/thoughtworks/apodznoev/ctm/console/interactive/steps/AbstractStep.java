package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import java.io.PrintWriter;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public abstract class AbstractStep implements InteractiveStep {
    protected final PrintWriter console;
    protected boolean failed;

    protected AbstractStep(PrintWriter console) {
        this.console = console;
    }

    @Override
    public boolean isFailed() {
        return failed;
    }

    protected void printYesNoQuestion() {
        console.println(getYesNoQuestion());
    }

    protected String getYesNoQuestion() {
        return "Type [Y/n]:";
    }


    protected static boolean parseYesNo(String yesNoAnswer) {
        if (yesNoAnswer.toLowerCase().equals("y")) {
            return true;
        }

        //just a simplification to not ask for exact input again
        return false;
    }
}
