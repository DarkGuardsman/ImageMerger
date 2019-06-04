package com.darkguardsman.image;

import com.darkguardsman.image.data.MergeData;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class ImageMerger
{

    public static void main(String... args)
    {
        if (args != null && args.length > 0)
        {
            HashMap<String, String> argMap = loadArgs(args);

            if (argMap.containsKey("json"))
            {
                json(new File(argMap.get("json")));
            }
            else if (argMap.containsKey("baseImages") && argMap.containsKey("mergeImages"))
            {
                final MergeData data = new MergeData();
                if (argMap.containsKey("output"))
                {
                    data.outputFolder = new File(argMap.get("output"));
                }
            }
            else
            {
                System.err.println("ImageMerger: Invalid arguments!");
                printHelp();
                System.exit(1);
            }
        }
        else
        {
            File file = findJsonFile();
            if (file != null)
            {
                json(file);
            }
            else
            {
                error("Failed to locate image-merger-run.json! " +
                        "Either place a file matching the name in the same directory as the run location " +
                        "or specify the data or path to the file.");
                printHelp();
                System.exit(2);
            }
        }
    }

    public static void printHelp()
    {

    }

    public static File findJsonFile()
    {
        final File folder = new File(System.getProperty("user.dir"));
        if (folder.exists() && folder.isDirectory())
        {
            for (File file : folder.listFiles())
            {
                if (file.getName().equalsIgnoreCase("image-merger-run.json"))
                {
                    return file;
                }
            }
        }
        return null;
    }

    public static void json(File file)
    {

    }

    public static void error(String msg)
    {
        System.err.println("ImageMerger: " + msg);
    }

    /**
     * Converts arguments into a hashmap for usage
     *
     * @param args
     * @return
     */
    public static HashMap<String, String> loadArgs(String... args)
    {
        final HashMap<String, String> map = new HashMap();
        if (args != null)
        {
            String currentArg = null;
            String currentValue = "";
            for (int i = 0; i < args.length; i++)
            {
                String next = args[i].trim();
                if (next == null)
                {
                    throw new IllegalArgumentException("Null argument detected in launch arguments");
                }
                else if (next.startsWith("-"))
                {
                    if (currentArg != null)
                    {
                        map.put(currentArg, currentValue);
                        currentValue = "";
                    }

                    if (next.contains("="))
                    {
                        String[] split = next.split("=");
                        currentArg = split[0].substring(1).trim();
                        currentValue = split[1].trim();
                        if (split.length > 2)
                        {
                            for (int l = 2; l < split.length; l++)
                            {
                                currentValue += "=" + split[l];
                            }
                        }
                    }
                    else
                    {
                        currentArg = next.substring(1).trim();
                    }
                }
                else if (currentArg != null)
                {
                    if (!currentValue.isEmpty())
                    {
                        currentValue += ",";
                    }
                    currentValue += next.replace("\"", "").replace("'", "").trim();
                }
                else
                {
                    throw new IllegalArgumentException("Value has no argument associated with it [" + next + "]");
                }
            }
            //Add the last loaded value to the map
            if (currentArg != null)
            {
                map.put(currentArg, currentValue);
            }
        }
        return map;
    }
}
