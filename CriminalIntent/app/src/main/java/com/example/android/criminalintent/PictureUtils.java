package com.example.android.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by jr on 2017-02-03.
 */

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // 파일의 이미지 크기를 알아낸다.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // 얼마나 크기를 조정할지 파악한다.
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Bitmap을 생성&반환
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(90); //-360~360


        Bitmap sideInversionImg = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);

        return sideInversionImg;
    }

}
