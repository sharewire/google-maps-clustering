package net.sharewire.googlemapsclustering;

import android.support.annotation.Nullable;

public interface ClusterItem extends QuadTreePoint {
    @Nullable
    String getTitle();

    @Nullable
    String getSnippet();
}
