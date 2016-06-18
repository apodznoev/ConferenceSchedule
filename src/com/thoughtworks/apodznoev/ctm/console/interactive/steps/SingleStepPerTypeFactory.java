package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;

import java.io.PrintWriter;

/**
 * todo
 * @author apodznoev
 * @since 18/06/16
 */
public class SingleStepPerTypeFactory implements StepsFactory {
    private final PrintWriter userPromter;
    public SingleStepPerTypeFactory(PrintWriter prompter) {
        this.userPromter = prompter;
    }

    @Override
    public InteractiveStep createStep(StepType stepType) {
        switch (stepType) {
            case CREATE_SCHEDULE:
                return new CreateScheduleStep(userPromter);
            case EDIT_SCHEDULE:
                return new EditScheduleStep(userPromter);
            case READ_FILE:
                return new ReadFileStep(userPromter);
            case EXPORT_SCHEDULE:
                return new ExportScheduleStep(userPromter);
            case READ_SCHEDULE:
                return new ReadScheduleStep(userPromter);
            default:
                throw new IllegalArgumentException("Add implementation for type:" + stepType);
        }
    }
}
