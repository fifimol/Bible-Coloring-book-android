package com.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import java.util.LinkedList;
import java.util.Queue;

class FloodFill {
    //library ,jangan ubah apapun di sini
    private Bitmap _bmp;
    private int _bmpHeight;
    private int _bmpWidth;
    private int _newColor;
    private int _oldColor;

    public FloodFill(Bitmap bmp, int oldColor, int newColor) {
        this._bmp = bmp;
        this._oldColor = oldColor;
        this._newColor = newColor;
        this._bmpWidth = bmp.getWidth();
        this._bmpHeight = bmp.getHeight();
    }

    public Bitmap getBitmap() {
        return this._bmp;
    }

    public Bitmap fill(int x, int y) {
        Queue<Point> queue = new LinkedList();
        if (this._bmp.getPixel(x, y) != this._oldColor) {
            return null;
        }
        queue.add(new Point(x, y));
        while (!queue.isEmpty()) {
            Point n = (Point) queue.poll();
            if (this._bmp.getPixel(n.x, n.y) == this._oldColor) {
                int wx = n.x;
                int ex = n.x + 1;
                while (this._bmp.getPixel(wx, n.y) == this._oldColor && wx >= 0) {
                    wx--;
                }
                while (this._bmp.getPixel(ex, n.y) == this._oldColor && ex <= this._bmpWidth - 1) {
                    ex++;
                }
                int ix = wx + 1;
                while (ix < ex) {
                    this._bmp.setPixel(ix, n.y, this._newColor);
                    if (n.y - 1 >= 0 && this._bmp.getPixel(ix, n.y - 1) == this._oldColor) {
                        queue.add(new Point(ix, n.y - 1));
                    }
                    if (n.y + 1 < this._bmpHeight && this._bmp.getPixel(ix, n.y + 1) == this._oldColor) {
                        queue.add(new Point(ix, n.y + 1));
                    }
                    ix++;
                }
            }
        }
        return this._bmp;
    }
}
