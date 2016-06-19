package com.thoughtworks.apodznoev.ctm.test;

import com.thoughtworks.apodznoev.ctm.console.interactive.stepdata.FilePathStepData;
import com.thoughtworks.apodznoev.ctm.console.interactive.steps.ReadFileStep;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * @author apodznoev
 * @since 18/06/16
 */
public class ReadFileStepTest {
    private ReadFileStep step;

    @Before
    public void setUp() throws Exception {
        initStep();
    }

    private void initStep() {
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
        step.doStep(getPathToTestClassFile().toString());
        assertTrue(step.isFinished());
        assertNotNull(step.getCollectedData());
        assertTrue(step.getCollectedData() instanceof FilePathStepData);
        FilePathStepData stepData = step.getCollectedData();
        assertNotNull(stepData.getFilePath());
        assertTrue(stepData.getFilePath().toFile().exists());
        assertFalse(stepData.getFilePath().toFile().isDirectory());
        assertTrue(stepData.getFilePath().toFile().canRead());
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

    @Test
    public void testFileIsTooBig() throws Exception {
        Path dir = getPathToTestClassFile().getParent();
        Path bigFilePath = dir.resolve("big_test.file");
        File bigFile = bigFilePath.toFile();
        bigFile.deleteOnExit();
        assumeTrue(bigFile.createNewFile());

        //definitely lower
        writeBytes(bigFilePath, 512 * 1024);
        step.doStep(bigFilePath.toString());
        //size is ok
        assertTrue(step.isFinished());
        initStep();

        //greater than one MB - need approve
        writeBytes(bigFilePath, 1025 * 1024);
        step.doStep(bigFilePath.toString());
        assertFalse(step.isFinished());
        step.doStep("dsd");
        //confirmation not passed, start from scratch
        assertFalse(step.isFinished());

        step.doStep(bigFilePath.toString());
        assertFalse(step.isFinished());
        step.doStep("Y");
        assertTrue(step.isFinished());
        assertNotNull(step.getCollectedData());
        assertTrue(bigFilePath.equals(step.getCollectedData().getFilePath()));
    }

    private void writeBytes(Path filePath, int bytesAmount) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(filePath);
        char[] chars = createRandomCharArray(bytesAmount);
        writer.write(chars);
        writer.flush();
        writer.close();
    }

    private char[] createRandomCharArray(int bytesSize) {
        char[] chars = new char[bytesSize / 2];
        Arrays.fill(chars, '\uffff');
        return chars;
    }

    private Path getPathToTestClassFile() {
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
        return currentDir.resolve(pathToThisFile);
    }
}
