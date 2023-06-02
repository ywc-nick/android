package com.example.project.test;

import android.graphics.Bitmap;

public class MyTest {

//
//
//        private Bitmap getCircularBitmap(Bitmap bitmap) {
//            // 计算圆形 Bitmap 基于原始 Bitmap 的缩放比例
//            float scalingFactor = Math.min(
//                    (float) getImageViewWidth() / bitmap.getWidth(),
//                    (float) getImageViewHeight() / bitmap.getHeight()
//            );
//
//            // 将原始 Bitmap 缩放到与 ImageView 相同的尺寸
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(
//                    bitmap,
//                    (int) (bitmap.getWidth() * scalingFactor),
//                    (int) (bitmap.getHeight() * scalingFactor),
//                    false
//            );
//
//            // 将原始 Bitmap 转换成圆形 Shape
//            Shape shape = new OvalShape();
//            shape.resize(scaledBitmap.getWidth(), scaledBitmap.getHeight());
//
//            // 将圆形 Shape 包装成 Drawable
//            ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
//            BitmapShader bitmapShader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            shapeDrawable.getPaint().setShader(bitmapShader);
//
//            // 创建一个新的 Bitmap 并绘制圆形 Drawable
//            Bitmap outputBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(outputBitmap);
//            shapeDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//            shapeDrawable.draw(canvas);
//
//            return outputBitmap;
//
//    }




}
