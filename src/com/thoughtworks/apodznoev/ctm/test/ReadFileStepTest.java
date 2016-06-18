package com.thoughtworks.apodznoev.ctm.test;

import com.thoughtworks.apodznoev.ctm.console.interactive.InteractiveConsoleProcessor;
import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.FilePathStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.ReadFileStep;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadFileStepTest {
    private ReadFileStep step;

    @Before
    public void setUp() throws Exception {
        step = new ReadFileStep(new PrintWriter(System.out));
    }

    @Test
    public void testFileNotExists() {
        Path currentDir = Paths.get(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
        );
        step.doStep(currentDir.resolve("notExists.file").toString());
        assertFalse(step.isFinished());
    }

    @Test
    public void testFileExistsAndReadable() throws Exception {
        Path currentDir = Paths.get(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
        );
        String pathToThisFile =
                getClass()
                        .getName()
                        .replaceAll("\\.", FileSystems.getDefault().getSeparator())
                        + ".class";
        step.doStep(currentDir.resolve(pathToThisFile).toString());
        assertTrue(step.isFinished());
        assertNotNull(step.getCollectedData());
        assertTrue(step.getCollectedData() instanceof FilePathStepData);
        FilePathStepData stepData = (FilePathStepData) step.getCollectedData();
        assertNotNull(stepData.getFile());
        assertTrue(stepData.getFile().exists());
        assertFalse(stepData.getFile().isDirectory());
        assertTrue(stepData.getFile().canRead());
    }

    @Test
    public void testDirectory() throws Exception {
        Path currentDir = Paths.get(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
        );
        step.doStep(currentDir.toString());
        assertFalse(step.isFinished());
    }
}
