package com.sid.coding.challenges.util;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import com.sid.coding.challenges.exceptions.JsonParseException;


public final class FileUtil
{
    public static String readFile(String filePath) throws JsonParseException
    {
        final File file = new File(filePath);
        final StringBuilder sb = new StringBuilder();

        String line;

        try (final BufferedReader reader = new BufferedReader(new FileReader(file)))
        {   
            while ((line = reader.readLine()) != null)
           {
               sb.append(line);
           }
        }
        catch (IOException e)
        {
            throw new JsonParseException("Error reading file: " + filePath, e);
        }

        return sb.toString();
    }
}
