package com.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import android.view.View;

public class GridSpacingItemDecoration extends ItemDecoration {

    private boolean includeEdge;
    private int spacing;
    private int spanCount;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % this.spanCount;
        if (this.includeEdge) {
            outRect.left = this.spacing - ((this.spacing * column) / this.spanCount);
            outRect.right = ((column + 1) * this.spacing) / this.spanCount;
            if (position < this.spanCount) {
                outRect.top = this.spacing;
            }
            outRect.bottom = this.spacing;
            return;
        }
        outRect.left = (this.spacing * column) / this.spanCount;
        outRect.right = this.spacing - (((column + 1) * this.spacing) / this.spanCount);
        if (position >= this.spanCount) {
            outRect.top = this.spacing;
        }
    }
}
