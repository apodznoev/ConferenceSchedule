package com.thoughtworks.apodznoev.ctm.console.interactive.stepdata;

import java.io.File;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class FilePathStepData implements StepData {
    private final File file;
    public FilePathStepData(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
