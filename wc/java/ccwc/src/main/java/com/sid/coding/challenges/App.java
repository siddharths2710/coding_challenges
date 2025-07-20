package com.sid.coding.challenges;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * print newline, word, and byte counts for each file! Just like wc command.
 *
 * @author sid
 * @version 1.0
 * @since 2025-07-06
 *
 */

public class App {

    private static Options ccwcOptions = new Options();

    static 
    {
        ccwcOptions.addOption(
            createOptions(
                "h", "help", "help", 
                "print this help", false, false));
        ccwcOptions.addOption(
            createOptions("l", "lines", "lines",
            "count lines", false, false));
        ccwcOptions.addOption(
            createOptions("w", "words", "words",
            "count words", false, false));
        ccwcOptions.addOption(
            createOptions("c", "bytes", "bytes",
            "print number of bytes", false, false));
    }
    public static void main(String[] args) {

        int exitCode = 0;

        final CommandLineParser parser = new DefaultParser();
        try {

            final CommandLine cmd = parser.parse(ccwcOptions, args);
            boolean hasHelp = false;
            boolean hasLine = false;
            boolean hasWord = false;
            boolean hasSize = false;

            if (cmd.hasOption("h") ||
               cmd.getArgList().isEmpty())
            {
                printHelp();
                hasHelp = true;
            }
            if (!hasHelp)
            {
                if (cmd.hasOption("l"))
                {
                    hasLine = true;
                }
                if (cmd.hasOption("w"))
                {
                    hasWord = true;
                }
                if (cmd.hasOption("c"))
                {
                    hasSize = true;
                }
                if (!(hasLine || hasWord || hasSize))
                {
                    hasLine = true;
                    hasWord = true;
                    hasSize = true;
                }

                final String[] files = cmd.getArgs();

                if (files.length == 0)
                {
                    System.err.println("No files specified");
                    printHelp();
                    exitCode = -1;
                }
                else
                {
                    int totalLines = 0;
                    int totalWords = 0;
                    int totalSize = 0;

                    for (final String fileString : files)
                    {
                        final File file = new File(fileString);

                        if (!file.exists())
                        {
                            System.err.println("File does not exist: " + fileString);
                            exitCode = -1;
                        }
                        else
                        {
                            try
                            {
                                if (hasLine)
                                {
                                    totalLines += countLines(fileString);
                                    System.out.print(totalLines + " ");
                                }
                                if (hasWord)
                                {
                                    totalWords += countWords(fileString);
                                    System.out.print(totalWords + " ");
                                }
                                if (hasSize)
                                {
                                    totalSize += countSizeInBytes(fileString);
                                    System.out.print(totalSize + " ");
                                }

                                System.out.println(file);
                            }
                            catch (IOException e)
                            {
                                System.err.println("Error reading file: " + fileString);
                                exitCode = -1;
                            }
                        }
                        if (exitCode != 0)
                        {
                            break;
                        }
                    }

                    if (exitCode == 0 && files.length > 1)
                    {
                        System.out.println(totalLines + " " + totalWords + " " + totalSize + " total");
                    }
                }
            }

            System.exit(exitCode);
        }
        catch (ParseException e)
        {
            System.err.println("Error parsing arguments: " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void printHelp()
    {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ccwc", ccwcOptions);
    }

    private static Option createOptions(
        String name,
        String longName,
        String argName,
        String description,
        boolean hasArg,
        boolean required)
    {
        return Option.builder(name)
            .longOpt(longName)
            .hasArg(hasArg)
            .argName(argName)
            .desc(description)
            .required(required)
            .build();
    }

    private static int countLines(String file)
       throws IOException
    {
        // read the entire file and count the number of line endings in the file.
        int lineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            while (reader.readLine() != null)
            {
                lineCount++;
            }
        }
        finally
        {
            //nothing here
        }

        return lineCount;
    }

    private static int countWords(String file)
       throws IOException
    {
        //count the number of words in the file.
        int wordCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                wordCount += line.split("\\s+").length;
            }
        }
        finally
        {
            //nothing here
        }

        return wordCount;
    }

    private static int countSizeInBytes(String file)
       throws IOException
    {
       //count the number of bytes in the file.
       int byteCount = 0;

       try (FileInputStream fis = new FileInputStream(file))
       {
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = fis.read(buffer)) != -1)
          {
             byteCount += bytesRead;
          }
       }
       finally
        {
            //nothing here
        }

        return byteCount;
    }
}
