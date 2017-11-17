package net.sharewire.googlemapsclustering;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import static net.sharewire.googlemapsclustering.Preconditions.checkNotNull;

/**
 * Represents the visual style of map marker icons. Supports customization
 * of individual attributes by setting their values with {@link Builder}.
 */
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

    /**
     * Returns the background color of cluster icons.
     *
     * @return the background color of cluster icons
     */
    @ColorInt
    public int getClusterBackgroundColor() {
        return clusterBackgroundColor;
    }

    /**
     * Returns the text color of cluster icons.
     *
     * @return the text color of cluster icons
     */
    @ColorInt
    public int getClusterTextColor() {
        return clusterTextColor;
    }

    /**
     * Returns the text color of cluster icons.
     *
     * @return the text color of cluster icons
     */
    @ColorInt
    public int getClusterStrokeColor() {
        return clusterStrokeColor;
    }

    /**
     * Returns the width of the stroke of cluster icons.
     *
     * @return the width of the stroke of cluster icons
     */
    public int getClusterStrokeWidth() {
        return clusterStrokeWidth;
    }

    /**
     * Returns the size of the text of cluster icons.
     *
     * @return the size of the text of cluster icons
     */
    public int getClusterTextSize() {
        return clusterTextSize;
    }

    /**
     * Returns the icon resource of cluster items.
     *
     * @return the icon resource of cluster items
     */
    @DrawableRes
    public int getClusterIconResId() {
        return clusterIconResId;
    }

    /**
     * The builder for {@link IconStyle}. Allows to customize different style attributes.
     * If a style attribute is not set explicitly, the default value will be used.
     */
    public static class Builder {

        private int clusterBackgroundColor;
        private int clusterTextColor;
        private int clusterStrokeColor;
        private int clusterStrokeWidth;
        private int clusterTextSize;
        private int clusterIconResId;

        /**
         * Creates a new builder with the default style.
         */
        public Builder(@NonNull Context context) {
            checkNotNull(context);
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

        /**
         * Sets the background color of cluster icons.
         *
         * @param color the background color of cluster icons
         * @return the builder instance
         */
        public Builder setClusterBackgroundColor(@ColorInt int color) {
            clusterBackgroundColor = color;
            return this;
        }

        /**
         * Sets the text color of cluster icons.
         *
         * @param color the text color of cluster icons
         * @return the builder instance
         */
        public Builder setClusterTextColor(@ColorInt int color) {
            clusterTextColor = color;
            return this;
        }

        /**
         * Sets the color of the stroke of cluster icons.
         *
         * @param color the color of the stroke of cluster icons
         * @return the builder instance
         */
        public Builder setClusterStrokeColor(@ColorInt int color) {
            clusterStrokeColor = color;
            return this;
        }

        /**
         * Sets the width of the stroke of cluster icons.
         *
         * @param width the width of the stroke of cluster icons
         * @return the builder instance
         */
        public Builder setClusterStrokeWidth(int width) {
            clusterStrokeWidth = width;
            return this;
        }

        /**
         * Sets the size of the text of cluster icons.
         *
         * @param size the size of the text of cluster icons
         * @return the builder instance
         */
        public Builder setClusterTextSize(int size) {
            clusterTextSize = size;
            return this;
        }

        /**
         * Sets the icon resource of cluster items.
         *
         * @param resId the icon resource of cluster items
         * @return the builder instance
         */
        public Builder setClusterIconResId(@DrawableRes int resId) {
            clusterIconResId = resId;
            return this;
        }

        /**
         * Creates a new instance of {@link IconStyle} with provided style attributes.
         *
         * @return new instance of {@link IconStyle} with provided style attributes
         */
        @NonNull
        public IconStyle build() {
            return new IconStyle(this);
        }
    }
}
