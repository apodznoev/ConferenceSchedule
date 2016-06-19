package com.thoughtworks.apodznoev.ctm.console.interactive;

import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.InteractiveStep;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.ReadFileStep;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.SingleStepPerTypeFactory;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.StepsFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class responsible for handling user's input in interactive question-answer mode.
 *
 * @author apodznoev
 * @since 17/06/16
 */
public class InteractiveConsoleProcessor {
    private final StepsFactory stepsFactory;
    private final PrintWriter console;
    private final PrintWriter errorOut;
    private CurrentState currentState;
    private InteractiveStep currentStep;
    private Map<Integer, StepType> nextStepChoice;
    private RestorePoint restorePoint;

    /**
     * @param writer whose data consumed by user.
     */
    public InteractiveConsoleProcessor(PrintWriter writer, PrintWriter errOut) {
        this.stepsFactory = new SingleStepPerTypeFactory(writer);
        this.console = writer;
        this.errorOut = errOut;
        this.currentState = CurrentState.DO_STEP;
        this.currentStep = new ReadFileStep(writer);
    }

    /**
     * Asks user for initial input.
     */
    public void askInitialQuestion() {
        console.println(currentStep.getInitialQuestion());
    }

    /**
     * Processes input supplied by user.
     * After each method iteration user will be asked for some other input.
     * <p>
     * In case if user's input provokes program termination, {@link #finished()} will return {@code true}
     *
     * @param input single line typed by user, not {@code null}
     * @throws IllegalStateException in case if method invoked after termination
     */
    public void processInput(String input) {
        if (currentState == CurrentState.FINISHED) {
            throw new IllegalStateException("No input expected after termination, but received:" + input);
        }

        if (currentState == CurrentState.DO_STEP) {
            currentStep.doStep(input);

            if (currentStep.isFailed()) {
                returnToPreviousStep();
                return;
            }

            if (!currentStep.isFinished()) {
                return;
            }

            processFinishedStep();
            return;
        }

        if (currentState == CurrentState.CHOOSE_NEXT_STEP) {
            processStepChoice(input);
        }
    }

    private void processFinishedStep() {
        StepType finishedType = currentStep.getStepType();
        Set<StepType> possibleNextSteps = StepType.findNextSuitable(finishedType);

        if (possibleNextSteps.isEmpty()) {
            //todo here possible to enhance the code to return to first step
            finish();
            return;
        }

        if (possibleNextSteps.size() == 1) {
            switchToNextStep(possibleNextSteps.iterator().next());
            return;
        }

        switchToNextStepChoice(possibleNextSteps);
    }

    private void finish() {
        currentState = CurrentState.FINISHED;
    }

    private void switchToNextStepChoice(Set<StepType> possibleNextSteps) {
        console.println("There are several opportunities to proceed, " +
                "please type the number for next desired action");
        currentState = CurrentState.CHOOSE_NEXT_STEP;
        nextStepChoice = new HashMap<>(possibleNextSteps.size());

        int stepId = 0;
        for (StepType step : possibleNextSteps) {
            nextStepChoice.put(++stepId, step);
        }

        printNextStepChoice();
    }

    private void printNextStepChoice() {
        nextStepChoice.forEach((choiceId, step) ->
                console.println(
                        String.format("%d. Step allow to: %s", choiceId, step.getStepDescription())
                )
        );

    }

    private void processStepChoice(String input) {
        int nextStepId = 0;

        try {
            nextStepId = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
        }

        if (!nextStepChoice.containsKey(nextStepId)) {
            console.println("Cannot recognize next step, please choose one of offered");
            printNextStepChoice();
            return;
        }

        switchToNextStep(nextStepChoice.get(nextStepId));
    }

    private void returnToPreviousStep() {
        console.println("Returning to previous step");
        if(restorePoint == null) {
            console.println("Unfortunately, return to previous step is not available, " +
                    "will terminate execution.");
            finish();
            return;
        }

        currentStep = restorePoint.createStepToRestore();
        currentState = CurrentState.DO_STEP;
        //we support only one step back right now
        restorePoint = null;
        console.println(currentStep.getInitialQuestion());
    }

    private void switchToNextStep(StepType nextStepType) {
        InteractiveStep nextStepImpl = findSuitableImplementation(nextStepType);
        StepData collectedData = currentStep.getCollectedData();
        currentStep = nextStepImpl;
        currentStep.supplyInitialData(collectedData);
        currentState = CurrentState.DO_STEP;
        restorePoint = new RestorePoint(nextStepType, collectedData);
        console.println(currentStep.getInitialQuestion());
    }

    private InteractiveStep findSuitableImplementation(StepType stepType) {
        return stepsFactory.createStep(stepType);
    }

    /**
     * Indicates that last processed input provoked program termination and
     * no other inputs expected.
     *
     * @return {@code true} in case if no other commands expected and thus
     * application can be shut down, {@code false} otherwise.
     */
    public boolean finished() {
        return currentState == CurrentState.FINISHED;
    }

    private enum CurrentState {
        DO_STEP,
        CHOOSE_NEXT_STEP,
        FINISHED
    }

    /**
     * Class allows to make step back from current state.
     */
    private class RestorePoint {
        private final StepType stepType;
        private final StepData stepData;

        private RestorePoint(StepType stepType, StepData stepData) {
            this.stepType = stepType;
            this.stepData = stepData;
        }

        private InteractiveStep createStepToRestore() {
            InteractiveStep restoreStep = findSuitableImplementation(stepType);
            restoreStep.supplyInitialData(stepData);
            return restoreStep;
        }
    }
}
