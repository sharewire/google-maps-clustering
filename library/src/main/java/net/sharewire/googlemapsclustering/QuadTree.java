package net.sharewire.googlemapsclustering;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class QuadTree<T extends QuadTreePoint> {

    private final int bucketSize;

    private QuadTreeNode<T> root;

    QuadTree(int bucketSize) {
        this.bucketSize = bucketSize;
        this.root = createRootNode(bucketSize);
    }

    void insert(@NonNull T point) {
        root.insert(point);
    }

    @NonNull
    List<T> queryRange(double north, double west, double south, double east) {
        List<T> points = new ArrayList<>();
        root.queryRange(new QuadTreeRect(north, west, south, east), points);
        return points;
    }

    void clear() {
        root = createRootNode(bucketSize);
    }

    @NonNull
    private QuadTreeNode<T> createRootNode(int bucketSize) {
        return new QuadTreeNode<>(90.0, -180.0, -90.0, 180.0, bucketSize);
    }
}
