package com.darkguardsman.image.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-06-04.
 */
public class ImageData
{

    public final File file;
    public BufferedImage image;

    public ImageData(File file)
    {
        this.file = file;
    }

    public void load()
    {
        try
        {
            image = ImageIO.read(file);
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to load image, " + file, e);
        }
    }

    public String getName()
    {
        return file.getName().substring(0, file.getName().lastIndexOf(".png"));
    }
}
