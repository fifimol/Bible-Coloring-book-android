package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class FloodFillImageView extends ImageView {
    //library ,jangan ubah apapun di sini
    List<Bitmap> bitmaps = new ArrayList();
    private Context context;
    int newColor;
    List<Bitmap> undoneBitmaps = new ArrayList();

    public FloodFillImageView(Context context) {
        super(context);
        this.context = context;
    }

    public FloodFillImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public FloodFillImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setColor(int newColor) {
        this.newColor = newColor;
    }

    public void setFloodFllBitmap(Bitmap floodFllBitmap) {
        setImageBitmap(getAlteredBitmap(floodFllBitmap));
        this.bitmaps.add(getAlteredBitmap(floodFllBitmap));
    }

    public void undo() {
        Log.d("undo()", "undo: size:" + this.bitmaps.size());
        if (this.bitmaps.size() > 1) {
            Bitmap bmp = (Bitmap) this.bitmaps.remove(this.bitmaps.size() - 1);
            setImageBitmap(getAlteredBitmap((Bitmap) this.bitmaps.get(this.bitmaps.size() - 1)));
            this.undoneBitmaps.add(bmp);
        }
    }

    public void redo() {
        Log.d("redo()", "redo: size:" + this.undoneBitmaps.size());
        if (this.undoneBitmaps.size() > 0) {
            Bitmap bmp = (Bitmap) this.undoneBitmaps.remove(this.undoneBitmaps.size() - 1);
            setImageBitmap(getAlteredBitmap(bmp));
            this.bitmaps.add(bmp);
        }
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                if (isEnabled()) {
                    BitmapDrawable drawable = (BitmapDrawable) getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Rect imageBounds = new Rect();
                    getDrawingRect(imageBounds);
                    int intrinsicHeight = drawable.getIntrinsicHeight();
                    int intrinsicWidth = drawable.getIntrinsicWidth();
                    float heightRatio = ((float) intrinsicHeight) / ((float) imageBounds.height());
                    float scaledImageOffsetX = event.getX() - ((float) imageBounds.left);
                    float scaledImageOffsetY = event.getY() - ((float) imageBounds.top);
                    int originalImageOffsetX = Math.round(scaledImageOffsetX * (((float) intrinsicWidth) / ((float) imageBounds.width())));
                    int originalImageOffsetY = Math.round(scaledImageOffsetY * heightRatio);
                    int color = bitmap.getPixel(originalImageOffsetX, originalImageOffsetY);
                    if (!(color == this.newColor || color == -16777216)) {
                        try {
                            FloodFill floodfill = new FloodFill(bitmap, color, this.newColor);
                            Log.d(getClass().getSimpleName(), "onTouch: (" + originalImageOffsetX + ", " + originalImageOffsetY + ")");
                            Bitmap floodFillBitmap = floodfill.fill(originalImageOffsetX, originalImageOffsetY);
                            if (floodFillBitmap != null) {
                                this.bitmaps.add(getAlteredBitmap(floodFillBitmap));
                                this.undoneBitmaps.clear();
                                invalidate();
                                break;
                            }
                            Log.e("tag", "onTouchEvent: floodFillBitmap == null");
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
                return false;
        }
        return false;
    }

    private Bitmap getAlteredBitmap(Bitmap bitmap) {
        Bitmap _alteredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        new Canvas(_alteredBitmap).drawBitmap(bitmap, new Matrix(), new Paint());
        return _alteredBitmap;
    }
}
