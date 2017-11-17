package net.sharewire.googlemapsclustering;

import android.support.annotation.Nullable;

/**
 * An object representing a single cluster item (marker) on the map.
 */
public interface ClusterItem extends QuadTreePoint {
    /**
     * The latitude of the item.
     *
     * @return the latitude of the item
     */
    @Override
    double getLatitude();

    /**
     * The longitude of the item.
     *
     * @return the longitude of the item
     */
    @Override
    double getLongitude();

    /**
     * The title of the item.
     *
     * @return the title of the item
     */
    @Nullable
    String getTitle();

    /**
     * The snippet of the item.
     *
     * @return the snippet of the item
     */
    @Nullable
    String getSnippet();
}
