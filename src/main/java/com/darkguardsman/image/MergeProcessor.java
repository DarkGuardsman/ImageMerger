package com.darkguardsman.image;

import com.darkguardsman.image.data.ImageData;
import com.darkguardsman.image.data.MergeData;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class MergeProcessor
{

    public static void process(MergeData data) throws IOException
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
        //Flatten folders down to file paths
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

        //Convert files to image data
        data.baseImages.addAll(flattenBase.stream().map(ImageData::new).collect(Collectors.toList()));
        data.mergeImages.addAll(flattenMerge.stream().map(ImageData::new).collect(Collectors.toList()));
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
        data.baseImageFiles.forEach(file -> validateFile(file));
        data.mergeImageFiles.forEach(file -> validateFile(file));
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
        data.baseImages.forEach(ImageData::load);
        data.mergeImages.forEach(ImageData::load);
    }

    public static void validateImages(MergeData data)
    {
        for (List<ImageData> list : new List[]{data.baseImages, data.mergeImages})
        {
            int width = list.get(0).image.getWidth();
            int height = list.get(0).image.getHeight();

            for (ImageData image : list)
            {
                final int w = image.image.getWidth();
                final int h = image.image.getHeight();
                if (w != width)
                {
                    ImageMerger.error("MergeProcessor >> Image widths do not match, expected " + width + " got " + w + ". File: " + image.file);
                }
                else if (h != height)
                {
                    ImageMerger.error("MergeProcessor >> Image heights do not match, expected " + height + " got " + h + ". File: " + image.file);
                }
            }
        }
    }

    public static void mergeImages(MergeData data) throws IOException
    {
        for (ImageData baseData : data.baseImages)
        {
            final BufferedImage base = baseData.image;
            final int w = base.getWidth();
            final int h = base.getHeight();

            //Merge all images down as 1 file onto the base
            if (data.mergeAll)
            {
                //Image to output
                final BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                //Paint to graphics
                Graphics g = combined.getGraphics();
                g.drawImage(base, 0, 0, null);

                //Name to save as, is appended each iteration of draw
                String name = baseData.getName();

                for (ImageData mergeData : data.mergeImages)
                {
                    name += "_" + mergeData.getName();
                    g.drawImage(mergeData.image, 0, 0, null);
                }

                //Output
                ImageIO.write(combined, "PNG", new File(data.outputFolder, name + ".png"));
            }
            //Merge 1 file at a time creating permutations
            else
            {
                for (ImageData mergeData : data.mergeImages)
                {
                    //Image to output
                    final BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                    //Paint to graphics
                    Graphics g = combined.getGraphics();
                    g.drawImage(base, 0, 0, null);
                    g.drawImage(mergeData.image, 0, 0, null);

                    //Output
                    String name = baseData.getName() + "_" + mergeData.getName();
                    ImageIO.write(combined, "PNG", new File(data.outputFolder, name + ".png"));
                }
            }
        }
    }
}
