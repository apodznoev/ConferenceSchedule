package com.thoughtworks.apodznoev.ctm.console.interactive.steps;


import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

/**
 * Common interface for logical unit in interactive console mode.
 * Typical usage is: to supply step with data from previous step,
 * print initial question to user, process user input, repeat question-process cycle
 * until all necessary data collected. Then finish the step and
 * provide collected from user data to next step.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public interface InteractiveStep {

    /**
     * Gets fixed type of the given step.
     */
    StepType getStepType();

    /**
     * Gets initial question which asked to user at first.
     *
     * @return human-readable string to ask the user
     */
    String getInitialQuestion();

    /**
     * Supplies this step with data received from previous step.
     *
     * @param initialData data collected by another step
     */
    void supplyInitialData(StepData initialData);

    /**
     * Handles single user input. Inside this method another questions can
     * be asked and some data generated. This method expected to be called several
     * times with inputs based on previously asked by this step questions.
     * First time input provided as a response to {@link #getInitialQuestion()}
     *
     * @param userInput non {@code null} text line typed by user
     */
    void doStep(String userInput);

    /**
     * Checks if given step is finished and collected data is ready to
     * be passed to next step.
     *
     * @return {@code true} in case if given step finished and no {@link #doStep(String)}
     * invocations expected, {@code false} otherwise
     */
    boolean isFinished();

    /**
     * Gets data collected by given step and possibly required by next step.
     * Will be provided as argument to {@link #supplyInitialData(StepData)}
     * to next step.
     *
     * @return some data collected from used during {@link #doStep(String)} calls
     */
    StepData getCollectedData();
}
