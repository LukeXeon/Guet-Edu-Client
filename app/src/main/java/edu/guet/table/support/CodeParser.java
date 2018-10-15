package edu.guet.table.support;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.小世界 on 2018/10/15.
 */

public final class CodeParser
{
    private CodeParser()
    {
    }

    private static int distance(int r_color, int g_color, int b_color)
    {
        int big = Math.max(r_color, Math.max(g_color, b_color));
        int small = Math.min(r_color, Math.min(g_color, b_color));
        int middle = Math.max(r_color, Math.min(g_color, b_color));
        return big + middle - small;
    }

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
                int color_total = getRed(color) + getGreen(color)
                        + getBlue(color);
                int distance = distance(getRed(color), getGreen(color),
                        getBlue(color));
                //if (color_total <= 360) {
                if (color_total <= 300)
                {
                    black_count++;
                } else if (getRed(color) + getGreen(color) <= 150
                        || getRed(color) + getBlue(color) <= 150
                        || getGreen(color) + getBlue(color) <= 150)
                {
                    black_count++;
                } else if (distance >= 190
                        && (getRed(color) < 100 || getGreen(color) < 100 || getBlue(color) < 100))
                {
                    black_count++;
                } else if (distance >= 250
                        && Math.min(getRed(color),
                        Math.min(getGreen(color), getBlue(color))) <= 50
                        && (getRed(color) < 100 || getGreen(color) < 100 || getBlue(color) < 100))
                {
                    black_count++;
                } else if (distance >= 180
                        && Math.min(getRed(color),
                        Math.max(getGreen(color), getBlue(color))) < 100
                        && Math.min(getRed(color),
                        Math.min(getGreen(color), getBlue(color))) < 100)
                {
                    black_count++;
                } else if (color_total <= 325
                        && Math.max(getRed(color),
                        Math.max(getGreen(color), getBlue(color))) < 120
                        && Math.min(getRed(color),
                        Math.min(getGreen(color), getBlue(color))) < 100)
                {
                    black_count++;
                }
//				 LogUtil.debug(TAG, "j="+j+",k="+k+",red="+ getRed(color)
//				 +",green="+ getGreen(color)+",blue="+ getBlue(color)
//				 +",distance="+distance+",is_black="+is_black);
            }
        }
        return black_count;
    }

    private static boolean checkLeftBottom(Bitmap img, int seq)
    {
        int begin_x = 0, begin_y = 7, end_x = 2, end_y = 8;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " left-bottom black_count=" + black_count);
        return black_count >= 3;
    }

    private static boolean checkTopTop(Bitmap img, int seq)
    {
        int begin_x = 0, begin_y = 0, end_x = 7, end_y = 0;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " top-top black_count=" + black_count);
        return black_count >= 4;
    }

    private static boolean checkLeftTop(Bitmap img, int seq)
    {
        int begin_x = 0, begin_y = 4, end_x = 2, end_y = 5;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " left-top black_count=" + black_count);
        return black_count >= 3;
    }

    private static boolean checkCenterCenter(Bitmap img, int seq)
    {
        int begin_x = 3, begin_y = 4, end_x = 4, end_y = 7;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " center-center black_count=" + black_count);
        return black_count >= 3;
    }

    private static boolean checkRightTop(Bitmap img, int seq)
    {
        int begin_x = 5, begin_y = 3, end_x = 7, end_y = 4;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " right-top black_count=" + black_count);
        return black_count >= 3;
    }

    private static boolean checkRightBottom(Bitmap img, int seq)
    {
        int begin_x = 5, begin_y = 8, end_x = 7, end_y = 9;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " right-bottom black_count=" + black_count);
        return black_count >= 3;
    }

    private static boolean checkBottomBottom(Bitmap img, int seq)
    {
        int begin_x = 0, begin_y = 10, end_x = 7, end_y = 11;
        int black_count = getBlackCount(img, begin_x, begin_y, end_x, end_y);
        //LogUtil.debug(TAG, "numberArray[seq]" + " bottom-bottom black_count=" + black_count);
        return black_count >= 6;
    }

    public static String parse(Bitmap img)
    {
        List<Bitmap> listImg = splitImage(img);
        String total_num = "";
        int number = 0;
        for (int i = 0; i < listImg.size(); i++)
        {
            if (!checkLeftBottom(listImg.get(i), i))
            { // 1、2、3、5、7、9
                if (!checkBottomBottom(listImg.get(i), i))
                { // 1、7
                    if (!checkTopTop(listImg.get(i), i))
                    {
                        number = 1;
                    } else
                    {
                        number = 7;
                    }
                } else
                { // 2、3、5、9
                    if (!checkLeftTop(listImg.get(i), i))
                    { // 2、3
                        if (!checkRightBottom(listImg.get(i), i))
                        {
                            number = 2;
                        } else
                        {
                            number = 3;
                        }
                    } else
                    { // 5、9
                        if (!checkRightTop(listImg.get(i), i))
                        {
                            number = 5;
                        } else
                        {
                            number = 9;
                        }
                    }
                }
            } else
            { // 0、4、6、8
                if (!checkBottomBottom(listImg.get(i), i))
                {
                    number = 4;
                } else
                {
                    if (!checkCenterCenter(listImg.get(i), i))
                    {
                        number = 0;
                    } else
                    { // 6、8
                        if (!checkRightTop(listImg.get(i), i))
                        {
                            number = 6;
                        } else
                        {
                            number = 8;
                        }
                    }
                }
            }
            total_num = total_num + number;
        }
        return total_num;
    }
}