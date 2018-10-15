package edu.guet.table.support;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */

public final class CodeParser
{
    private CodeParser() {}
    private static int getRed(String color)
    {
        return Integer.parseInt(color.substring(2, 4), 16);
    }
    private static int getGreen(String color)
    {
        return Integer.parseInt(color.substring(4, 6), 16);
    }
    private static int getBlue(String color)
    {
        return Integer.parseInt(color.substring(6, 8), 16);
    }
    private static List<Bitmap> splitImage(Bitmap img)
            throws Exception
    {
        List<Bitmap> subImgs = new ArrayList<>();
        subImgs.add(Bitmap.createBitmap(img, 6, 5, 8, 12));
        subImgs.add(Bitmap.createBitmap(img, 15, 5, 8, 12));
        subImgs.add(Bitmap.createBitmap(img, 24, 5, 8, 12));
        subImgs.add(Bitmap.createBitmap(img, 33, 5, 8, 12));
        return subImgs;
    }
    private static int getBlackCount(Bitmap img, int begin_x,
                                     int begin_y, int end_x, int end_y)
    {
        int black_count = 0;
        for (int j = begin_x; j <= end_x; j++)
        {
            for (int k = begin_y; k <= end_y; k++)
            {
                String color = Integer.toHexString(img.getPixel(j, k));
                // Color color = new Color(img.getRGB(j, k));
                int color_total = ImageUtil.getRed(color) + ImageUtil.getGreen(color)
                        + ImageUtil.getBlue(color);
                int distance = ImageUtil.distance(ImageUtil.getRed(color), ImageUtil.getGreen(color),
                        ImageUtil.getBlue(color));
                //if (color_total <= 360) {
                if (color_total <= 300)
                {
                    black_count++;
                } else if (ImageUtil.getRed(color) + ImageUtil.getGreen(color) <= 150
                        || ImageUtil.getRed(color) + ImageUtil.getBlue(color) <= 150
                        || ImageUtil.getGreen(color) + ImageUtil.getBlue(color) <= 150)
                {
                    black_count++;
                } else if (distance >= 190
                        && (ImageUtil.getRed(color) < 100 || ImageUtil.getGreen(color) < 100 || ImageUtil.getBlue(color) < 100))
                {
                    black_count++;
                } else if (distance >= 250
                        && Math.min(ImageUtil.getRed(color),
                        Math.min(ImageUtil.getGreen(color), ImageUtil.getBlue(color))) <= 50
                        && (ImageUtil.getRed(color) < 100 || ImageUtil.getGreen(color) < 100 || ImageUtil.getBlue(color) < 100))
                {
                    black_count++;
                } else if (distance >= 180
                        && Math.min(ImageUtil.getRed(color),
                        Math.max(ImageUtil.getGreen(color), ImageUtil.getBlue(color))) < 100
                        && Math.min(ImageUtil.getRed(color),
                        Math.min(ImageUtil.getGreen(color), ImageUtil.getBlue(color))) < 100)
                {
                    black_count++;
                } else if (color_total <= 325
                        && Math.max(ImageUtil.getRed(color),
                        Math.max(ImageUtil.getGreen(color), ImageUtil.getBlue(color))) < 120
                        && Math.min(ImageUtil.getRed(color),
                        Math.min(ImageUtil.getGreen(color), ImageUtil.getBlue(color))) < 100)
                {
                    black_count++;
                }
//				 LogUtil.debug(TAG, "j="+j+",k="+k+",red="+ImageUtil.getRed(color)
//				 +",green="+ImageUtil.getGreen(color)+",blue="+ImageUtil.getBlue(color)
//				 +",distance="+distance+",is_black="+is_black);
            }
        }
        return black_count;
    }
}
