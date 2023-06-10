package com.example.project.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Base64;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author yangwenchuan
 */
public class ImageUtils {


    public static final int bigimage = 20;
    public static final int smallimage = 10;

    public static Bitmap getBitMap(String path) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    public static Bitmap String2Bitmap(String bytes){
        byte[] imagebytes = Base64.decode(bytes,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
    }

    public static Bitmap getRoundedCornerBitmap(String image, int pixels) {
        byte[] imagebytes = Base64.decode(image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes,0,imagebytes.length);
        // 创建一个ARGB_8888格式的Bitmap对象，大小等于传入的位图对象
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建一个Canvas对象，将新生成的Bitmap对象作为绘图目标
        Canvas canvas = new Canvas(output);

        // 定义绘制用的变量 边框颜色
        final int color = 0xff424242;
        final Paint paint = new Paint();
        // 创建Rect对象，大小与原位图相同
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // 创建RectF对象，将rect变量转换为float型，并为后续转换为圆角矩形时提供参考
        final RectF rectF = new RectF(rect);
        // 定义拐角半径
        final float roundPx = pixels;

        // 开启整体抗锯齿
        paint.setAntiAlias(true);
        // 创建透明的颜色值，第一参数代表透明度，后面三个参数代表颜色
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        // drawRoundRect方法来绘制圆角矩形，第一个参数为要绘制的矩形，
        // 第二个和第三个参数分别为水平圆角半径和垂直圆角半径
        // 绘制圆角矩形
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        // 设置xfermode，它是一种指定如何将绘制的颜色与目标颜色混合的东西
        // 这里指明SRC_IN模式，那么画笔只在圆角矩形内画出来，外面不显示
        // 将SRC_IN模式，后面的绘制内容仅仅会覆盖目标图形中已有的内容，
        // 其余的仍然维持透明或者是之前所绘制的内容。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 利用Canvas绘制圆角位图
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // 返回圆角位图
        return output;
    }


    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int diameter = Math.min(w, h);//获取Bitmap对象的宽度和高度，找出较小的值作为圆的直径大小。

        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);//创建一个新的Bitmap对象，将其设置为ARGB_8888格式，然后创建一个Canvas对象，使我们能够在其上绘图
        Canvas canvas = new Canvas(output);

        //创建画笔对象并设置其为抗锯齿，然后设置颜色为白色。还创建一个Rect对象，将它指定为新Bitmap对象的矩形范围。
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Rect rect = new Rect(0, 0, diameter, diameter);
        //设置Bitmap对象在画布中的坐标位置，然后使用Canvas绘制出一个透明的ARGB颜色矩形，
        // 再在其中添加一个圆形作为遮罩层。此时，输出效果为一个白色的圆形（因为我们并没有绘制Bitmap对象）。
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        //通过设置PorterDuffXfermode来指定Canvas的绘制操作，这将告诉Canvas仅应使用SRC_IN模式绘制位图。然后用Canvas绘制原始图像
        // ，然后绘制圆形遮罩层，该遮罩层将限制Bitmap对象的可见区域为圆形。透明部分变得透明，有颜色的部分则需要根据模式进行前后颜色进行混合后输出。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);

        return output;
    }


}
