package com.darkguardsman.image;

import com.darkguardsman.image.data.MergeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class ImageMerger
{

    public static void main(String... args) throws IOException
    {
        info("Starting...");
        info("Args: " + Arrays.toString(args));
        if (args != null && args.length > 0)
        {
            final HashMap<String, String> argMap = loadArgs(args);

            if (argMap.containsKey("json"))
            {
                info("Json path set to '" + argMap.get("json") + "'");
                json(new File(argMap.get("json")));
            }
            else if (argMap.containsKey("baseImages") && argMap.containsKey("mergeImages"))
            {
                final MergeData data = new MergeData();
                if (argMap.containsKey("output"))
                {
                    data.outputFolder = new File(argMap.get("output"));
                }
                if(argMap.containsKey("merge"))
                {
                    data.mergeAll = true;
                }
                if(argMap.containsKey("permutate"))
                {
                    data.mergeAll = false;
                }

                //Get image paths
                final String baseImageString = argMap.get("baseImages");
                final String mergeImageString = argMap.get("mergeImages");

                info("Base Image Paths: " + baseImageString);
                info("Merge Image Paths: " + mergeImageString);

                //Split
                String[] baseImages = baseImageString.split(";");
                String[] mergeImages = mergeImageString.split(";");

                //Convert to files
                data.baseImageFiles.addAll(Arrays.stream(baseImages).map(File::new).collect(Collectors.toList()));
                data.mergeImageFiles.addAll(Arrays.stream(mergeImages).map(File::new).collect(Collectors.toList()));

                //Run
                MergeProcessor.process(new File(System.getProperty("user.dir")), data);
            }
            else
            {
                error("Invalid arguments!", false);
                printHelp();
                System.exit(1);
            }
        }
        else
        {
            File file = findJsonFile();
            if (file != null)
            {
                info("Found json at " + file);
                json(file);
            }
            else
            {
                error("Failed to locate image-merger-run.json! " +
                        "Either place a file matching the name in the same directory as the run location " +
                        "or specify the data or path to the file.", false);
                printHelp();
                System.exit(2);
            }
        }

        System.out.println("Completed...");
        System.exit(0);
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

    public static void json(File file) throws IOException
    {
        if(file.exists())
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            JsonParser parser = new JsonParser();
            JsonObject jsonData = parser.parse(br).getAsJsonObject();

            final MergeData mergeData = new MergeData();
            if (jsonData.has("output"))
            {
                mergeData.outputFolder = new File(jsonData.getAsJsonPrimitive("output").getAsString());
            }
            if(jsonData.has("merge"))
            {
                mergeData.mergeAll = jsonData.get("merge").getAsBoolean();
            }

            //Load files
            JsonArray baseImages = jsonData.getAsJsonArray("base");
            JsonArray mergeImages = jsonData.getAsJsonArray("merge");

            for(JsonElement element : baseImages)
            {
                mergeData.baseImageFiles.add(new File(element.getAsString()));
            }
            for(JsonElement element : mergeImages)
            {
                mergeData.mergeImageFiles.add(new File(element.getAsString()));
            }

            //Run
            MergeProcessor.process(file.getParentFile(), mergeData);
        }
        else
        {
            error("Failed to locate json file " + file.getAbsolutePath(), true);
        }
    }

    public static void error(String msg, boolean exit)
    {
        System.err.println("[ImageMerger] " + msg);
        if(exit)
        {
            System.exit(1);
        }
    }

    public static void info(String msg)
    {
        System.out.println("[ImageMerger] " + msg);
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
