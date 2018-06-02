package com.gustavogoma.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.GONE;

/**
 * @author Gustavo Araújo
 */

public class ImageTools extends Thread {

    private String mImageUrl;
    private OnSuccessListener mOnSuccessListener;
    private OnFailedListener mOnFailedListener;

    private ImageTools() {
        this.mImageUrl = "";
        this.mOnSuccessListener = new OnSuccessListener() {
            @Override
            public void onImageDownloaded(Bitmap bitmap) {
            }
        };
        this.mOnFailedListener = new OnFailedListener() {
            @Override
            public void onImageDownloadFailed(Exception e) {
            }
        };
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        try {
            URL url = new URL(mImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            this.mOnFailedListener.onImageDownloadFailed(e);
        }

        this.mOnSuccessListener.onImageDownloaded(bitmap);
    }

    public ImageTools onSuccess(@NonNull OnSuccessListener onSuccessListener) {
        this.mOnSuccessListener = onSuccessListener;
        return this;
    }

    public ImageTools onFailed(@NonNull OnFailedListener onFailedListener) {
        this.mOnFailedListener = onFailedListener;
        return this;
    }

    private interface OnSuccessListener {
        void onImageDownloaded(Bitmap bitmap);
    }

    private interface OnFailedListener {
        void onImageDownloadFailed(Exception e);
    }

    /**
     * Decode the byte array to a Bitmap.
     *
     * @param byteArray Bitmap byte array
     * @return Bitmap
     */
    public static Bitmap byteArrayToBitmap(@NonNull byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Encode the Bitmap to a byte array.
     *
     * @param bitmap Bitmap byte array
     * @return byte[] Encoded bitmap
     */
    public static byte[] bitmapToByteArray(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Downloads a image through its url.
     *
     * @param imageUrl Image URL
     * @return ImageTools instance. It contains the events of the download.
     */
    public static ImageTools download(@NonNull String imageUrl) {
        ImageTools imgTools = new ImageTools();
        imgTools.mImageUrl = imageUrl;

        return imgTools;
    }

    /**
     * Set the image into ImageView and, if the ProgressBar isn't null, set its visibility as GONE.
     *
     * @param imageView     ImageView where the image will be shown
     * @param progressBar ProgressBar that, if not null, will be hidden
     * @param bitmap      Image to be shown
     */
    public static void setImage(@NonNull ImageView imageView,
                                @NonNull Bitmap bitmap,
                                @Nullable ProgressBar progressBar) {
        imageView.setImageBitmap(bitmap);
        imageView.setAlpha(1f);
        imageView.setBackground(null);
        imageView.setPadding(0, 0, 0, 0);
        if (progressBar != null) progressBar.setVisibility(GONE);
    }

    /**
     * Returns a squared image
     *
     * @param bmp Image to be cropped
     * @return Bitmap Cropped image
     */
    public static Bitmap getSquareBitmap(@NonNull Bitmap bmp) {
        final int x;
        final int y;
        final int width;
        final int height;

        if (bmp.getWidth() >= bmp.getHeight()) {
            x = bmp.getWidth() / 2 - bmp.getHeight() / 2;
            y = 0;
            width = bmp.getHeight();
            height = bmp.getHeight();
        } else {
            x = 0;
            y = bmp.getHeight() / 2 - bmp.getWidth() / 2;
            width = bmp.getWidth();
            height = bmp.getWidth();
        }

        return Bitmap.createBitmap(bmp, x, y, width, height);
    }

    /**
     * Returns a circled image
     *
     * @param bitmap Image to be cropped
     * @return Bitmap Cropped image
     */
    public static Bitmap getCircularBitmap(@NonNull Bitmap bitmap) {
        return getRoundedCornerBitmap(getSquareBitmap(bitmap), bitmap.getWidth());
    }

    /**
     * * Returns a rounded corner image
     *
     * @param bitmap Image to be cropped
     * @param pixels Corner radius
     * @return Bitmap Cropped image
     */
    public static Bitmap getRoundedCornerBitmap(@NonNull Bitmap bitmap, int pixels) {
        final int color = 0xff424242;
        final float roundPx = pixels;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
