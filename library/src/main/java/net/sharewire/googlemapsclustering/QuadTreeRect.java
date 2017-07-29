package net.sharewire.googlemapsclustering;

import android.support.annotation.NonNull;

class QuadTreeRect {

    final double north;
    final double west;
    final double south;
    final double east;

    QuadTreeRect(double north, double west, double south, double east) {
        this.north = north;
        this.west = west;
        this.south = south;
        this.east = east;
    }

    boolean contains(double latitude, double longitude) {
        return longitude >= west && longitude <= east && latitude <= north && latitude >= south;
    }

    boolean intersects(@NonNull QuadTreeRect bounds) {
        return west <= bounds.east && east >= bounds.west && north >= bounds.south && south <= bounds.north;
    }
}
