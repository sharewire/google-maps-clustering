package com.sharewire.googlemapsclustering.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import net.sharewire.googlemapsclustering.ClusterItem;

class SampleClusterItem implements ClusterItem {

    private final LatLng location;

    SampleClusterItem(@NonNull LatLng location) {
        this.location = location;
    }

    @Override
    public double getLatitude() {
        return location.latitude;
    }

    @Override
    public double getLongitude() {
        return location.longitude;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }

    @Nullable
    @Override
    public float getColor() {
        return BitmapDescriptorFactory.HUE_AZURE;
    }
}
