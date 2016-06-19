package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.PrintWriter;

/**
 * Too many features, too low time.
 *
 * @author apodznoev
 * @since 19/06/16
 */
public abstract class NotImplementedStep extends AbstractStep {
    protected NotImplementedStep(PrintWriter console) {
        super(console);
    }

    @Override
    public String getInitialQuestion() {
        return "Unfortunately you are using trial version of " +
                "Conference Track Management application!\n" +
                "Please press any key to return.";
    }

    @Override
    public void supplyInitialData(StepData initialData) {

    }

    @Override
    public void doStep(String userInput) {
        failed = true;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public StepData getCollectedData() {
        return null;
    }
}
