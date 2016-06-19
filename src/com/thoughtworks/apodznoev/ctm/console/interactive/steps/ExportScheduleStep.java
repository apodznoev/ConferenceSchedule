package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.ScheduleStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;
import com.thoughtworks.apodznoev.ctm.domain.schedules.ConferenceSchedule;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.ConsoleObjectWriter;
import com.thoughtworks.apodznoev.ctm.domain.tools.export.FileObjectWriter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Step allows to export schedule from previous step either to file, either
 * to console.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class ExportScheduleStep extends AbstractStep {
    private static final int PRINT_SCREEN_OPTION_ID = 1;
    private static final int EXPORT_TO_FILE_OPTION_ID = 2;

    private final ConsoleObjectWriter<ConferenceSchedule> consoleWriter;
    private final FileObjectWriter<ConferenceSchedule> fileWriter;

    private ConferenceSchedule schedule;
    private CurrentState stepState = CurrentState.FORMAT_CONFIRMATION;

    public ExportScheduleStep(PrintWriter userPrompter,
                              ConsoleObjectWriter<ConferenceSchedule> consoleWriter,
                              FileObjectWriter<ConferenceSchedule> fileWriter) {
        super(userPrompter);
        this.consoleWriter = consoleWriter;
        this.fileWriter = fileWriter;
    }

    @Override
    public StepType getStepType() {
        return StepType.EXPORT_SCHEDULE;
    }

    @Override
    public String getInitialQuestion() {
        return "In which format do you fish to export schedule?\n" + getPossibleOptionsQuestion();
    }

    private String getPossibleOptionsQuestion() {
        return "Possible options:\n" +
                PRINT_SCREEN_OPTION_ID + ". Print to screen\n" +
                EXPORT_TO_FILE_OPTION_ID + ". Export to text file";
    }

    @Override
    public void supplyInitialData(StepData initialData) {
        if (!(initialData instanceof ScheduleStepData)) {
            throw new IllegalStateException(
                    "Unexpected initial data, expected to be " + ScheduleStepData.class +
                            " but is: " + initialData.getClass()
            );
        }

        this.schedule = ((ScheduleStepData) initialData).getSchedule();
    }

    @Override
    public void doStep(String userInput) {
        if (stepState == CurrentState.FORMAT_CONFIRMATION) {
            parseFormatChoice(userInput);
            if (stepState == CurrentState.SCREEN_PRINTING) {
                printSchedule();
                stepState = CurrentState.FINISHED;
            }
            return;
        }

        if (stepState == CurrentState.WAITING_EXPORT_FILE_LOCATION) {
            if (tryExportToFile(userInput)) {
                stepState = CurrentState.FINISHED;
            }
        }
    }

    private boolean tryExportToFile(String filePath) {
        Path path = Paths.get(filePath);
        File file = path.toFile();
        if (!validateFile(file)) {
            return false;
        }

        try {
            fileWriter.writeObject(schedule, path);
        } catch (IOException e) {
            console.println("Unexpected exception during write to file:" + e.getMessage());
            return false;
        }

        console.println("Schedule successfully written.");
        return true;
    }

    private boolean validateFile(File file) {
        if (file.exists() && file.isDirectory()) {
            console.println("Specified path points to directory, please enter correct location");
            return false;
        }

        if (file.exists() && !file.canWrite() && !file.setWritable(true)) {
            console.println("Permission denied, please enter accessible file");
            return false;
        }

        if (!file.exists()) {
            boolean success;
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                success = false;
            }
            if (!success) {
                console.println("Cannot create file with given location");
                return false;
            }
        }

        return true;
    }

    private void printSchedule() {
        console.print("Printing schedule");
        printWaiter();
        consoleWriter.writeObject(schedule, console);
    }

    private void printWaiter() {
        //just for fun
        for (int i = 0; i < 100; i++) {
            console.print(".");
        }
        console.println();
    }

    private void parseFormatChoice(String userInput) {
        int chosenOption = 0;
        try {
            chosenOption = Integer.parseInt(userInput);
        } catch (NumberFormatException ignored) {
        }

        if (chosenOption == PRINT_SCREEN_OPTION_ID) {
            stepState = CurrentState.SCREEN_PRINTING;
            return;
        }

        if (chosenOption == EXPORT_TO_FILE_OPTION_ID) {
            console.println("Please specify location of file for export");
            console.println("Note! Existing file will be override, not existing created.");
            stepState = CurrentState.WAITING_EXPORT_FILE_LOCATION;
            return;
        }

        console.println("Cannot recognize option.");
        console.println(getPossibleOptionsQuestion());
    }

    @Override
    public boolean isFinished() {
        return stepState == CurrentState.FINISHED;
    }

    @Override
    public StepData getCollectedData() {
        //does not provide data
        return null;
    }

    private enum CurrentState {
        FORMAT_CONFIRMATION,
        SCREEN_PRINTING,
        WAITING_EXPORT_FILE_LOCATION,
        FINISHED
    }
}
