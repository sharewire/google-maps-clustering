package net.sharewire.googlemapsclustering;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

class SquareTextView extends TextView {

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        //noinspection SuspiciousNameCombination
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
