package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public interface StepsFactory {

    //todo
    InteractiveStep createStep(StepType stepType);
}
