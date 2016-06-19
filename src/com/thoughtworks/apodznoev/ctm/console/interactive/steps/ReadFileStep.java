package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.FilePathStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.text.DecimalFormat;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadFileStep extends AbstractStep {
    private static final long FILE_SIZE_WARNING_THRESHOLD = 1024 * 1024;

    private CurrentState stepState = CurrentState.NORMAL;
    private String lastInput;
    private boolean ignoreSize;
    private FilePathStepData stepData;

    public ReadFileStep(PrintWriter userPrompter) {
        super(userPrompter);
    }

    @Override
    public StepType getStepType() {
        return StepType.READ_FILE;
    }

    @Override
    public String getInitialQuestion() {
        return "Enter path to file for conference generation.";
    }

    @Override
    public void supplyInitialData(StepData initialData) {
        //no data needed
    }

    @Override
    public void doStep(String userInput) {
        if (stepState == CurrentState.SIZE_CONFIRMATION) {
            boolean confirmationReceived = handleFileSizeConfirmation(userInput);
            if(!confirmationReceived)
                return;

            userInput = lastInput;
        }

        handleFilePathInput(userInput);
    }

    private void handleFilePathInput(String userInput) {
        try {
            lastInput = userInput;
            Path path = Paths.get(userInput).normalize();
            if (validateFile(path.toFile())) {
                stepData = new FilePathStepData(path);
            }
        } catch (InvalidPathException e) {
            console.println("Cannot obtain file by given path due to:" + e.getMessage());
            printRepeat();
        }
    }

    private boolean handleFileSizeConfirmation(String yesNoAnswer) {
        stepState = CurrentState.NORMAL;
        boolean ignoreFileSize = parseYesNo(yesNoAnswer);

        if (!ignoreFileSize){
            console.println(getInitialQuestion());
            return false;
        }

        console.println("Ignoring file size");
        ignoreSize = true;
        return true;
    }

    private void printRepeat() {
        console.println("Please correct your input:");
    }

    private boolean validateFile(File file) {
        if (!file.exists()) {
            console.println("File with given path does not exists");
            printRepeat();
            return false;
        }

        if (file.isDirectory()) {
            console.println("Given path points to directory");
            printRepeat();
            return false;
        }

        if (!file.canRead()) {
            console.println("Given file cannot be read, please check permissions");
            printRepeat();
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (!ignoreSize) {
            return validateFileSize(file);
        }

        return true;
    }

    private boolean validateFileSize(File file) {
        long fileSize;
        try {
            fileSize = Files.size(file.toPath());
        } catch (IOException e) {
            console.println("Failed to read size of file due to:" + e.getMessage());
            return false;
        }

        if (fileSize >= FILE_SIZE_WARNING_THRESHOLD) {
            stepState = CurrentState.SIZE_CONFIRMATION;
            console.println("Passed file has a size:" + readableFileSize(fileSize) +
                    ", do you really want to continue?");
            printYesNoQuestion();
            return false;
        }

        return true;
    }

    @Override
    public boolean isFinished() {
        return stepData != null;
    }

    @Override
    public FilePathStepData getCollectedData() {
        return stepData;
    }

    //yes, that's a copy-paste code
    private static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private enum CurrentState {
        NORMAL,
        SIZE_CONFIRMATION
    }
}
