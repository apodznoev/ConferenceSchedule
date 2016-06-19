package com.thoughtworks.apodznoev.ctm.console;

import com.thoughtworks.apodznoev.ctm.console.interactive.InteractiveConsoleProcessor;

import java.io.Console;
import java.io.PrintWriter;

/**
 * Main entry point for application.
 * Expects that was launched using console directly without stream redirection and not as daemon.
 *
 * @author apodznoev
 * @since 17/06/16
 */
public class ConsoleLauncher {
    private static final String EXIT_COMMAND = "\\q";

    public static void main(String... args) {
        Console console = System.console();
        if (console == null) {
            System.err.println(
                    "Please, run this application from interactive command line, without redirecting of in/out streams."
            );
            return;
        }
        PrintWriter writer = console.writer();
        PrintWriter errorWriter = new PrintWriter(System.err);
        InteractiveConsoleProcessor processor = new InteractiveConsoleProcessor(writer);

        writer.println("Welcome to Conference Track Management application!");
        writer.println("This application will help you to create your perfect schedule for any (almost) conference " +
                "you'll want!");
        writer.println("To generate appropriate schedule please follow the applications instructions.");
        writer.println();
        writer.println("You can exit the application at any time by typing '" + EXIT_COMMAND + "'.");
        writer.println();
        processor.askInitialQuestion();

        while (true) {
            String input = console.readLine();
            if (input == null || EXIT_COMMAND.equals(input)) {
                writer.println("Thanks for using Conference Track Management application!");
                break;
            }

            try {
                processor.processInput(input);
            } catch (Exception e) {
                writer.println("Unexpected exception, will terminate immediately");
                break;
            }

            if (processor.finished())
                break;
        }

        writer.close();
        errorWriter.close();
    }
}
