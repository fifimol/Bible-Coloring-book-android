package com.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.biblestory.color.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Constants {
    public static final String ASSETS_DRAWING_DIRECTORY = "drawings";
    public static final String FONT_NAME = "fonts/BAUHS93.ttf";
    public static final String KEY_DRAWING_NAME = "imagepath";
    public static String[] colors = new String[]{"#FFFFFF",
            "#FF0000",
            "#FFA500",
            "#FFFF00",
            "#0BF60B",
            "#00FFFF",
            "#0000FF",
            "#800080",
            "#FFC0CB",
            "#FF00FF",
            "#008000",
            "#FFD700",
            "#C0C0C0",
            "#FF00B4",
            "#00F5FF",
            "#F0FFF0",
            "#794044",
            "#000000",
            "#FFFF00",
            "#1CE6FF",
            "#FF34FF", "#FF4A46", "#008941", "#006FA6", "#A30059", "#FFDBE5", "#7A4900", "#0000A6", "#63FFAC", "#B79762", "#004D43", "#8FB0FF", "#997D87", "#5A0007", "#809693", "#FEFFE6", "#1B4400", "#4FC601", "#3B5DFF", "#4A3B53", "#FF2F80", "#61615A", "#BA0900", "#6B7900", "#00C2A0", "#FFAA92", "#FF90C9", "#B903AA", "#D16100", "#DDEFFF", "#000035", "#7B4F4B", "#A1C299", "#300018", "#0AA6D8", "#013349", "#00846F", "#372101", "#FFB500", "#C2FFED", "#A079BF", "#CC0744", "#C0B9B2", "#C2FF99", "#001E09", "#00489C", "#6F0062", "#0CBD66", "#EEC3FF", "#456D75", "#B77B68", "#7A87A1", "#788D66", "#885578", "#FAD09F", "#FF8A9A", "#D157A0", "#BEC459", "#456648", "#0086ED", "#886F4C", "#34362D", "#B4A8BD", "#00A6AA", "#452C2C", "#636375", "#A3C8C9", "#FF913F", "#938A81", "#575329", "#00FECF", "#B05B6F", "#8CD0FF", "#3B9700", "#04F757", "#C8A1A1", "#1E6E00", "#7900D7", "#A77500", "#6367A9", "#A05837", "#6B002C", "#772600", "#D790FF", "#9B9700", "#549E79", "#FFF69F", "#201625", "#72418F", "#BC23FF", "#99ADC0", "#3A2465", "#922329", "#5B4534", "#FDE8DC", "#404E55", "#0089A3", "#CB7E98", "#A4E804", "#324E72", "#6A3A4C", "#83AB58", "#001C1E", "#D1F7CE", "#004B28", "#C8D0F6", "#A3A489", "#806C66", "#222800", "#BF5650", "#E83000", "#66796D", "#DA007C", "#FF1A59", "#8ADBB4", "#1E0200", "#5B4E51", "#C895C5", "#320033", "#FF6832", "#66E1D3"};

    public static int getColor(int position) {
        try {
            return Color.parseColor(colors[position]);
        } catch (Exception e) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
    }

    public static void clickAnimation(View view) {
        Animation anim = new ScaleAnimation(TextTrackStyle.DEFAULT_FONT_SCALE, 0.8f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.8f, 1, 0.5f, 1, 0.5f);
        anim.setFillAfter(false);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    public static boolean shareBitmap(Context context, Bitmap bitmap, String textToShare, String intentMessage) {
        File file = new File(context.getFilesDir(), "Share.png");
        saveBitmapAtLocation(bitmap, CompressFormat.PNG, 100, file);
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");

        intent.putExtra("android.intent.extra.STREAM", contentUri);
        String textToAppend = context.getString(R.string.rate_app_play) + context.getPackageName();
        if (textToShare == null || textToShare.isEmpty()) {
            intent.putExtra("android.intent.extra.TEXT", textToAppend);
        } else {
            intent.putExtra("android.intent.extra.TEXT", new StringBuilder(String.valueOf(textToShare)).append(" ").append(textToAppend).toString());
        }
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, intentMessage));
        return true;
    }

    public static String saveBitmapToGallery(Context context, Bitmap image) {
        String appName = getAppName(context);
        File savedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), appName);
        savedFile.mkdirs();
        File savedFile2 = new File(savedFile, new StringBuilder(String.valueOf(appName)).append("_").append(Calendar.getInstance().getTimeInMillis()).append(".png").toString());
        if (savedFile2.exists()) {
            savedFile2.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(savedFile2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        image.compress(CompressFormat.PNG, 100, bos);
        try {
            bos.flush();
            bos.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            MediaScannerConnection.scanFile(context, new String[]{savedFile2.getAbsolutePath()}, null, null);
            return savedFile2.getAbsolutePath();
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String getAppName(Context context) {
        ApplicationInfo ai;
        PackageManager pm = context.getApplicationContext().getPackageManager();
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    public static void saveBitmapAtLocation(Bitmap bitmap, CompressFormat format, int quality, File location) {
        IOException e;
        if (location.exists()) {
            location.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(location);
            FileOutputStream fileOutputStream2;
            try {
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                bitmap.compress(format, quality, bos);
                bos.flush();
                bos.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                fileOutputStream2 = fileOutputStream;
            } catch (IOException e2) {
                e = e2;
                fileOutputStream2 = fileOutputStream;
                e.printStackTrace();
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
        v.draw(new Canvas(b));
        return b;
    }

    public static File getMyArtworksDirectory(Context context) {
        return context.getExternalFilesDir("MyArtwork");
    }

    public static int dpToPx(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics()));
    }
}
