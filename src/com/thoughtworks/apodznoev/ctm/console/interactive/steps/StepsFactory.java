package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;

/**
 * Factory which provided concrete implementation for steps of given type.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public interface StepsFactory {

    /**
     * Creates interactive step instance for given step type
     */
    InteractiveStep createStep(StepType stepType);
}
