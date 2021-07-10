package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.biblestory.color.R;


public class BrushView extends View {

    //library ,jangan ubah apapun di sini
    private int alphaValue = 255;
    private Paint circle;
    private int color = -16776961;
    private int left = 10;
    private int radius;
    private Paint square;
    private int top = 10;

    public BrushView(Context context) {
        super(context);
        initWith(context, null);
    }

    public BrushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWith(context, attrs);
    }

    public BrushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWith(context, attrs);
    }


    @SuppressLint("ResourceType")
    private void initWith(Context context, AttributeSet attrs) {
        this.circle = new Paint();
        this.square = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BrushView);
        this.radius = typedArray.getInt(0, 5);
        this.alphaValue = typedArray.getInt(2, 255);
        this.color = typedArray.getInt(1, -16776961);
        this.square.setAntiAlias(true);
        this.square.setStyle(Style.STROKE);
        this.square.setStrokeWidth(5.0f);
        this.square.setColor(-16776961);
        typedArray.recycle();
    }

    public void setSquareColor(int color) {
        this.square.setColor(color);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.circle.setAntiAlias(true);
        this.circle.setStyle(Style.FILL);
        this.circle.setColor(this.color);
        this.circle.setAlpha(this.alphaValue);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) getRadius(), this.circle);
        canvas.drawRect((float) this.left, (float) this.top, (float) (getWidth() - this.left), (float) (getHeight() - this.top), this.square);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public int getRadius() {
        return this.radius;
    }

    public void setColor(int color) {
        this.color = color;
        this.circle.setColor(color);
        invalidate();
    }

    public int getColor() {
        return this.color;
    }

    public void setAlphaValue(int alphaValue) {
        this.alphaValue = alphaValue;
        this.circle.setAlpha(alphaValue);
        invalidate();
    }

    public int getAlphaValue() {
        return this.alphaValue;
    }
}
