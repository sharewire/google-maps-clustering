package net.sharewire.googlemapsclustering;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public class IconStyle {

    private final int clusterBackgroundColor;
    private final int clusterTextColor;
    private final int clusterStrokeColor;
    private final int clusterStrokeWidth;
    private final int clusterTextSize;
    private final int clusterIconResId;

    private IconStyle(@NonNull Builder builder) {
        clusterBackgroundColor = builder.clusterBackgroundColor;
        clusterTextColor = builder.clusterTextColor;
        clusterStrokeColor = builder.clusterStrokeColor;
        clusterStrokeWidth = builder.clusterStrokeWidth;
        clusterTextSize = builder.clusterTextSize;
        clusterIconResId = builder.clusterIconResId;
    }

    @ColorInt
    public int getClusterBackgroundColor() {
        return clusterBackgroundColor;
    }

    @ColorInt
    public int getClusterTextColor() {
        return clusterTextColor;
    }

    @ColorInt
    public int getClusterStrokeColor() {
        return clusterStrokeColor;
    }

    public int getClusterStrokeWidth() {
        return clusterStrokeWidth;
    }

    public int getClusterTextSize() {
        return clusterTextSize;
    }

    @DrawableRes
    public int getClusterIconResId() {
        return clusterIconResId;
    }

    public static class Builder {

        private int clusterBackgroundColor;
        private int clusterTextColor;
        private int clusterStrokeColor;
        private int clusterStrokeWidth;
        private int clusterTextSize;
        private int clusterIconResId;

        public Builder(@NonNull Context context) {
            clusterBackgroundColor = ContextCompat.getColor(
                    context, R.color.cluster_background);
            clusterTextColor = ContextCompat.getColor(
                    context, R.color.cluster_text);
            clusterStrokeColor = ContextCompat.getColor(
                    context, R.color.cluster_stroke);
            clusterStrokeWidth = context.getResources()
                    .getDimensionPixelSize(R.dimen.cluster_stroke_width);
            clusterTextSize = context.getResources()
                    .getDimensionPixelSize(R.dimen.cluster_text_size);
            clusterIconResId = R.drawable.ic_map_marker;
        }

        public Builder setClusterBackgroundColor(@ColorInt int color) {
            clusterBackgroundColor = color;
            return this;
        }

        public Builder setClusterTextColor(@ColorInt int color) {
            clusterTextColor = color;
            return this;
        }

        public Builder setClusterStrokeColor(@ColorInt int color) {
            clusterStrokeColor = color;
            return this;
        }

        public Builder setClusterStrokeWidth(int width) {
            clusterStrokeWidth = width;
            return this;
        }

        public Builder setClusterTextSize(int size) {
            clusterTextSize = size;
            return this;
        }

        public Builder setClusterIconResId(@DrawableRes int resId) {
            clusterIconResId = resId;
            return this;
        }

        @NonNull
        public IconStyle build() {
            return new IconStyle(this);
        }
    }
}
