package com.thoughtworks.apodznoev.ctm.console.interactive.steps;

import com.thoughtworks.apodznoev.ctm.console.interactive.StepType;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.FilePathStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.StepData;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadFileStep extends AbstractStep {
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
        try {
            Path path = Paths.get(userInput).normalize();
            File file = path.toFile();
            if(validateFile(file)) {
                stepData = new FilePathStepData(file);
            }
        }catch (InvalidPathException e) {
            console.println("Cannot obtain file by given path due to:" + e.getMessage());
            printRepeat();
        }
    }

    private void printRepeat() {
        console.println("Please correct your input:");
    }

    private boolean validateFile(File file) {
        if(!file.exists()) {
            console.println("File with given path does not exists");
            printRepeat();
            return false;
        }

        if(file.isDirectory()){
            console.println("Given path points to directory");
            printRepeat();
            return false;
        }

        if(!file.canRead()){
            console.println("Given file cannot be read, please check permissions");
            printRepeat();
            return false;
        }

        return true;
    }

    @Override
    public boolean isFinished() {
        return stepData != null;
    }

    @Override
    public StepData getCollectedData() {
        return stepData;
    }
}
