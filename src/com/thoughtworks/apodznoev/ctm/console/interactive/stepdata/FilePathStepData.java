package com.thoughtworks.apodznoev.ctm.console.interactive.stepdata;

import java.nio.file.Path;

/**
 * Step data from steps which received from users some file location.
 *
 * @author apodznoev
 * @since 18/06/16
 */
public class FilePathStepData implements StepData {
    private final Path file;

    public FilePathStepData(Path file) {
        this.file = file;
    }

    public Path getFilePath() {
        return file;
    }
}
