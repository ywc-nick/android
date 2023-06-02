package com.example.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * @author yangwenchuan
 */
public class ImageUtils {



    public static Bitmap Stream2Bitmap(InputStream inputStream){

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
