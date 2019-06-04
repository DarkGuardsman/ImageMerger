package com.darkguardsman.image.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class MergeData
{

    public final List<File> baseImageFiles = new ArrayList();
    public final List<File> mergeImageFiles = new ArrayList();

    public final List<ImageData> baseImages = new ArrayList();
    public final List<ImageData> mergeImages = new ArrayList();

    public File outputFolder;

    public boolean mergeAll = true;

    public void setup()
    {
        if (outputFolder == null)
        {
            outputFolder = new File(System.getProperty("user.dir"), "output");
        }

        //Create output
        if (!outputFolder.exists())
        {
            outputFolder.mkdirs();
        }
    }
}
