package net.sharewire.googlemapsclustering;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.BitmapDescriptor;

public interface IconGenerator<T extends ClusterItem> {
    @NonNull
    BitmapDescriptor getClusterIcon(@NonNull Cluster<T> cluster);

    @NonNull
    BitmapDescriptor getClusterItemIcon(@NonNull T clusterItem);
}
