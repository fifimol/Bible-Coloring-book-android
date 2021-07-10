package com.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import java.util.ArrayList;

public class DrawingPanel extends View implements OnTouchListener {
    //library ,jangan ubah apapun di sini
    private static final float TOUCH_TOLERANCE = 0.0f;
    private boolean isNeedToReset;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private float mX;
    private float mY;
    private ArrayList<Paint> paints = new ArrayList();
    private ArrayList<Path> paths = new ArrayList();
    private ArrayList<Paint> undonePaints = new ArrayList();
    private ArrayList<Path> undonePaths = new ArrayList();

    public DrawingPanel(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);
        this.mCanvas = new Canvas();
        this.mPath = new Path();
    }

    public void colorChanged(int color) {
        this.mPaint.setColor(color);
    }

    public void changePaint(Paint aPaint) {
        this.mPaint = new Paint(aPaint);
    }

    public boolean undo() {
        if (this.paths.size() > 0) {
            this.undonePaths.add((Path) this.paths.remove(this.paths.size() - 1));
            this.undonePaints.add((Paint) this.paints.remove(this.paints.size() - 1));
            invalidate();
        }
        return this.paths.size() > 0;
    }

    public boolean redo() {
        if (this.undonePaths.size() > 0) {
            this.paths.add((Path) this.undonePaths.remove(this.undonePaths.size() - 1));
            this.paints.add((Paint) this.undonePaints.remove(this.undonePaints.size() - 1));
            invalidate();
        }
        return this.undonePaths.size() > 0;
    }

    public void reset() {
        this.paths.clear();
        this.paints.clear();
        this.undonePaths.clear();
        this.undonePaints.clear();
        this.isNeedToReset = true;
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < this.paths.size(); i++) {
            canvas.drawPath((Path) this.paths.get(i), (Paint) this.paints.get(i));
        }
        canvas.drawPath(this.mPath, this.mPaint);
        if (this.isNeedToReset) {
            this.isNeedToReset = false;
            Paint paint = new Paint();
            paint.setColor(0);
            paint.setStyle(Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);
            canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), paint);
        }
    }

    private void touch_start(float x, float y) {
        this.mPath.reset();
        this.mPath.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= 0.0f || dy >= 0.0f) {
            this.mPath.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            this.mX = x;
            this.mY = y;
        }
    }

    private void touch_up() {
        this.mPath.lineTo(this.mX, this.mY);
        this.mCanvas.drawPath(this.mPath, this.mPaint);
        this.paths.add(this.mPath);
        this.paints.add(this.mPaint);
        this.mPath = new Path();
        this.undonePaths.clear();
        this.undonePaints.clear();
    }

    public boolean onTouch(View arg0, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                touch_start(x, y);
                invalidate();
                break;
            case 1:
                touch_up();
                invalidate();
                break;
            case 2:
                touch_move(x, y);
                invalidate();
                break;
        }
        return true;
    }
}
