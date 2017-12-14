package net.sharewire.googlemapsclustering;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * An object representing a cluster of items (markers) on the map.
 */
public class Cluster<T extends ClusterItem> {

    private final double latitude;
    private final double longitude;
    private final List<T> items;
    private final double north;
    private final double west;
    private final double south;
    private final double east;

    Cluster(double latitude, double longitude, @NonNull List<T> items,
            double north, double west, double south, double east) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.items = items;
        this.north = north;
        this.west = west;
        this.south = south;
        this.east = east;
    }

    /**
     * The latitude of the cluster.
     *
     * @return the latitude of the cluster
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * The longitude of the cluster.
     *
     * @return the longitude of the cluster
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * The items contained in the cluster.
     *
     * @return the items contained in the cluster
     */
    @NonNull
    public List<T> getItems() {
        return items;
    }

    boolean contains(double latitude, double longitude) {
        return longitude >= west && longitude <= east
                && latitude <= north && latitude >= south;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return Double.compare(cluster.latitude, latitude) == 0 &&
                Double.compare(cluster.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
