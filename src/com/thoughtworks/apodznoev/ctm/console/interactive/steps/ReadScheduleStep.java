package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.PrintWriter;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadScheduleStep extends AbstractStep {
    public ReadScheduleStep(PrintWriter userPromter) {
        super(userPromter);
    }

    @Override
    public StepType getStepType() {
        return StepType.READ_SCHEDULE;
    }

    @Override
    public String getInitialQuestion() {
        return "todo here ask for question";
    }

    @Override
    public void supplyInitialData(StepData initialData) {

    }

    @Override
    public void doStep(String userInput) {
        console.println("Echo:" + userInput);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public StepData getCollectedData() {
        return new StepData() {
        };
    }
}
