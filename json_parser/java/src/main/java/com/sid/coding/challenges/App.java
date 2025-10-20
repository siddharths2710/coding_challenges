package com.sid.coding.challenges;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * Code which will parse a JSON file or a JSON string and print the results.
 * 
 * The JSON file or string will be provided as a command line argument.
 * 
 * @author sid
 * @version 1.0
 * @since 2025-07-20
 *
 */
public class App {

    private static final Options options = new Options();

    static
    {
        options.addOption(createOption(
            "f", "file", "file",
            "JSON file to parse", true, false));

        options.addOption(createOption(
            "s", "string", "string",
            "JSON string content to parse", true, false));

        options.addOption(createOption(
            "h", "help", "help",
            "Print this help message", false, false));

    }
    public static void main(String[] args)
    {
        int exitCode = 0;
        
        final CommandLineParser parser = new DefaultParser();
        
        boolean isHelp = false;
        
        boolean isFile = false;
        boolean isString = false;

        try
        {
            final CommandLine commandLine = parser.parse(options, args);

            isHelp = commandLine.hasOption("h");
            isFile = commandLine.hasOption("f");
            isString = commandLine.hasOption("s");

            if (!(isHelp || isFile || isString))
            {
                System.err.println("Error: Either a JSON file or a JSON string must be provided");
                exitCode = 1;
            }

            if (isHelp)
            {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar json-parser.jar", options);
            }
            else if (isFile)
            {
                final String jsonFile = commandLine.getOptionValue("f");
                
            }
            else
            {
                final String jsonString = commandLine.getOptionValue("s");
            }
        }
        catch (ParseException e)
        {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            exitCode = 1;
        }
    }

    private static Option createOption(
        String name,
        String longName,
        String argName,
        String description,
        boolean hasArg,
        boolean isRequired)
    {
        return Option.builder(name)
            .longOpt(longName)
            .hasArg(hasArg)
            .argName(argName)
            .desc(description)
            .required(isRequired)
            .build();
    }
}
