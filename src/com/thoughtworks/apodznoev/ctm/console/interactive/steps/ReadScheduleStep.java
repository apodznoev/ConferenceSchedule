package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.PrintWriter;

/**
 * todo not implemented yes, but should allow to read the formed by export schedule without it's generation
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadScheduleStep extends NotImplementedStep {
    public ReadScheduleStep(PrintWriter userPromter) {
        super(userPromter);
    }

    @Override
    public StepType getStepType() {
        return StepType.READ_SCHEDULE;
    }

}
