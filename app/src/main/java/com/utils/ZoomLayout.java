package com.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class ZoomLayout extends FrameLayout implements OnScaleGestureListener {
    //library ,jangan ubah apapun di sini
    private static final float MAX_ZOOM = 4.0f;
    private static final float MIN_ZOOM = 1.0f;
    private static final String TAG = "ZoomLayout";
    private float dx = 0.0f;
    private float dy = 0.0f;
    private float lastScaleFactor = 0.0f;
    private Mode mode = Mode.NONE;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;
    private float scale = 1.0f;
    private float startX = 0.0f;
    private float startY = 0.0f;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    public ZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & 255) {
                    case 0:
                        Log.i(ZoomLayout.TAG, "DOWN");
                        if (ZoomLayout.this.scale > 1.0f) {
                            ZoomLayout.this.mode = Mode.DRAG;
                            ZoomLayout.this.startX = motionEvent.getX() - ZoomLayout.this.prevDx;
                            ZoomLayout.this.startY = motionEvent.getY() - ZoomLayout.this.prevDy;
                            break;
                        }
                        break;
                    case 1:
                        Log.i(ZoomLayout.TAG, "UP");
                        ZoomLayout.this.mode = Mode.NONE;
                        ZoomLayout.this.prevDx = ZoomLayout.this.dx;
                        ZoomLayout.this.prevDy = ZoomLayout.this.dy;
                        break;
                    case 2:
                        if (ZoomLayout.this.mode == Mode.DRAG) {
                            ZoomLayout.this.dx = motionEvent.getX() - ZoomLayout.this.startX;
                            ZoomLayout.this.dy = motionEvent.getY() - ZoomLayout.this.startY;
                            break;
                        }
                        break;
                    case 5:
                        ZoomLayout.this.mode = Mode.ZOOM;
                        break;
                    case 6:
                        ZoomLayout.this.mode = Mode.NONE;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);
                if ((ZoomLayout.this.mode == Mode.DRAG && ZoomLayout.this.scale >= 1.0f) || ZoomLayout.this.mode == Mode.ZOOM) {
                    ZoomLayout.this.getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = ((((float) ZoomLayout.this.child().getWidth()) - (((float) ZoomLayout.this.child().getWidth()) / ZoomLayout.this.scale)) / 2.0f) * ZoomLayout.this.scale;
                    float maxDy = ((((float) ZoomLayout.this.child().getHeight()) - (((float) ZoomLayout.this.child().getHeight()) / ZoomLayout.this.scale)) / 2.0f) * ZoomLayout.this.scale;
                    ZoomLayout.this.dx = Math.min(Math.max(ZoomLayout.this.dx, -maxDx), maxDx);
                    ZoomLayout.this.dy = Math.min(Math.max(ZoomLayout.this.dy, -maxDy), maxDy);
                    Log.i(ZoomLayout.TAG, "Width: " + ZoomLayout.this.child().getWidth() + ", scale " + ZoomLayout.this.scale + ", dx " + ZoomLayout.this.dx + ", max " + maxDx);
                    ZoomLayout.this.applyScaleAndTranslation();
                }
                return true;
            }
        });
    }

    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleBegin");
        return true;
    }

    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        Log.i(TAG, "onScale" + scaleFactor);
        if (this.lastScaleFactor == 0.0f || Math.signum(scaleFactor) == Math.signum(this.lastScaleFactor)) {
            this.scale *= scaleFactor;
            this.scale = Math.max(1.0f, Math.min(this.scale, MAX_ZOOM));
            this.lastScaleFactor = scaleFactor;
        } else {
            this.lastScaleFactor = 0.0f;
        }
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scaleDetector) {
        Log.i(TAG, "onScaleEnd");
    }

    private void applyScaleAndTranslation() {
        child().setScaleX(this.scale);
        child().setScaleY(this.scale);
        child().setTranslationX(this.dx);
        child().setTranslationY(this.dy);
    }

    public void resetScale() {
        this.scale = 1.0f;
        this.prevDy = 0.0f;
        this.prevDx = 0.0f;
        this.dy = 0.0f;
        this.dx = 0.0f;
        this.startY = 0.0f;
        this.startX = 0.0f;
        this.lastScaleFactor = 0.0f;
        applyScaleAndTranslation();
    }

    private View child() {
        return getChildAt(0);
    }
}
