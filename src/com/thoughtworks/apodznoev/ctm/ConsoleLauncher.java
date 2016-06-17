package com.thoughtworks.apodznoev.ctm;

import com.thoughtworks.apodznoev.ctm.temp.TempClass;

import java.io.Console;
import java.io.PrintWriter;

/**
 * @author apodznoev
 * @since 17/06/16
 */
public class ConsoleLauncher {
    private static final String EXIT_COMMAND = "\\q";

    public static void main(String... args) {
        TempClass tempClass = new TempClass();
        Console console = System.console();
        if (console == null) {
            System.err.println(
                    "Please, run this application from interactive command line, without redirecting of in/out streams."
            );
            return;
        }
        PrintWriter writer = console.writer();

        writer.println("Welcome to Conference Track Management application!");
        writer.println("This application will help you to create your perfect schedule for any (almost) conference " +
                "you'll want!");
        writer.println("To generate appropriate schedule please follow the applications instructions.");
        writer.println();
        writer.println("You can exit the application at any time by typing '" + EXIT_COMMAND + "'.");

        while (true) {
            String input = console.readLine();
            if (EXIT_COMMAND.equals(input)) {
                writer.println("Thanks for using Conference Track Management application!");
                break;
            }
            writer.println("Echo:" + input);
        }
    }
}
