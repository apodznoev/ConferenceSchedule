package com.thoughtworks.apodznoev.ctm.console.interactive;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum which describes all possible allowed actions and their flow in interactive console regime,
 * using {@link InteractiveConsoleProcessor} class.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public enum StepType {
    READ_FILE("read file to parse/generate conference schedule"),
    CREATE_SCHEDULE("generate new conference schedule based on file data", READ_FILE),
    READ_SCHEDULE("parse already composed conference schedule from file", READ_FILE),
    EDIT_SCHEDULE("modify existing schedule", CREATE_SCHEDULE, READ_SCHEDULE),
    EXPORT_SCHEDULE("export schedule", CREATE_SCHEDULE, EDIT_SCHEDULE, READ_SCHEDULE);

    private final String stepDescription;
    private final Set<StepType> preSteps;

    /**
     * @param stepDescription human-readable step description
     * @param preSteps        steps from which it's allowed to be switched to given step,
     */
    StepType(String stepDescription, StepType... preSteps) {
        this.stepDescription = stepDescription;
        this.preSteps = new HashSet<>(Arrays.asList(preSteps));
    }

    /**
     * Finds all steps which can be executed after the passed one.
     *
     * @param previous some step.
     * @return unordered set of steps which can be executed after the given one
     */
    public static Set<StepType> findNextSuitable(StepType previous) {
        Set<StepType> allNextPossible = EnumSet.noneOf(StepType.class);

        for (StepType next : values()) {
            if (next.preSteps.contains(previous)) {
                allNextPossible.add(next);
            }
        }

        return allNextPossible;
    }

    /**
     * Gets human-readable step description
     *
     * @return string describing actions performed in given step
     */
    public String getStepDescription() {
        return stepDescription;
    }
}
