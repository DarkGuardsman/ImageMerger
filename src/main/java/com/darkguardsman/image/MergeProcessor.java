package com.darkguardsman.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class MergeProcessor
{

    public static void process(MergeData data)
    {
        data.setup();

        flattenFiles(data);
        validateFiles(data);
        loadImages(data);
        validateImages(data);
        mergeImages(data);
    }

    public static void flattenFiles(MergeData data)
    {
        final List<File> flattenBase = new ArrayList();
        for (File file : data.baseImageFiles)
        {
            flatten(file, flattenBase);
        }

        final List<File> flattenMerge = new ArrayList();
        for (File file : data.mergeImageFiles)
        {
            flatten(file, flattenBase);
        }

        data.baseImageFiles.clear();
        data.baseImageFiles.addAll(flattenBase);

        data.mergeImageFiles.clear();
        data.mergeImageFiles.addAll(flattenMerge);
    }

    private static void flatten(File folder, List<File> list)
    {
        if (folder.exists() && folder.isDirectory())
        {
            for (File file : folder.listFiles())
            {
                flatten(file, list);
            }
        }
        else if (!folder.exists() || folder.getName().endsWith(".png"))
        {
            //Exists errors are handled later, we just want to flatten to get all .png nested in sub folders
            list.add(folder);
        }
    }

    public static void validateFiles(MergeData data)
    {
        data.baseImageFiles.forEach(file -> file.exists() && file.getName().endsWith(.png));
        data.mergeImageFiles.forEach(file -> file.exists());
    }

    public static void validateFile(File file)
    {
        if (!file.exists())
        {
            ImageMerger.error("Failed to locate file " + file);
        }
        else if (!file.getName().endsWith(".png"))
        {
            ImageMerger.error("Only .png files are supported " + file);
        }
    }

    public static void loadImages(MergeData data)
    {

    }

    public static void validateImages(MergeData data)
    {

    }

    public static void mergeImages(MergeData data)
    {

    }
}
