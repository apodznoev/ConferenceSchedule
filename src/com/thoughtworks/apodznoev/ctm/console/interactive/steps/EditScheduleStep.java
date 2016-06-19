package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.PrintWriter;

/**
 * todo not implemented yet, but will allow to manage lectures within in-memory schedule
 * @author apodznoev
 * @since 18/06/16
 */
public class EditScheduleStep extends NotImplementedStep {
    public EditScheduleStep(PrintWriter userPrompter) {
        super(userPrompter);
    }

    @Override
    public StepType getStepType() {
        return StepType.EDIT_SCHEDULE;
    }
}
