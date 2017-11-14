package net.sharewire.googlemapsclustering;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import static net.sharewire.googlemapsclustering.Preconditions.checkNotNull;

public class ClusterManager<T extends ClusterItem> implements GoogleMap.OnCameraIdleListener {

    private static final int QUAD_TREE_BUCKET_CAPACITY = 4;

    private final GoogleMap mGoogleMap;

    private final QuadTree<T> mQuadTree;

    private final ClusterRenderer<T> mRenderer;

    private AsyncTask mQuadTreeTask;

    private AsyncTask mClusterTask;

    public interface Callbacks<T extends ClusterItem> {
        boolean onClusterClick(@NonNull Cluster<T> cluster);

        boolean onClusterItemClick(@NonNull T clusterItem);
    }

    public ClusterManager(@NonNull Context context, @NonNull GoogleMap googleMap) {
        checkNotNull(context);
        mGoogleMap = checkNotNull(googleMap);
        mRenderer = new ClusterRenderer<>(context, googleMap);
        mQuadTree = new QuadTree<>(QUAD_TREE_BUCKET_CAPACITY);
    }

    public void setIconGenerator(@NonNull IconGenerator<T> iconGenerator) {
        checkNotNull(iconGenerator);
        mRenderer.setIconGenerator(iconGenerator);
    }

    public void setCallbacks(@Nullable Callbacks<T> callbacks) {
        mRenderer.setCallbacks(callbacks);
    }

    public void setItems(@NonNull List<T> clusterItems) {
        checkNotNull(clusterItems);
        buildQuadTree(clusterItems);
    }

    @Override
    public void onCameraIdle() {
        cluster();
    }

    private void buildQuadTree(@NonNull List<T> clusterItems) {
        if (mQuadTreeTask != null) {
            mQuadTreeTask.cancel(true);
        }

        mQuadTreeTask = new QuadTreeTask(clusterItems).execute();
    }

    private void cluster() {
        if (mClusterTask != null) {
            mClusterTask.cancel(true);
        }

        mClusterTask = new ClusterTask(mGoogleMap.getProjection().getVisibleRegion().latLngBounds,
                mGoogleMap.getCameraPosition().zoom).execute();
    }

    @NonNull
    private List<Cluster<T>> getClusters(@NonNull LatLngBounds latLngBounds, float zoomLevel) {
        List<Cluster<T>> clusters = new ArrayList<>();

        long tileCount = (long) (Math.pow(2, zoomLevel) * 2);

        double startLatitude = latLngBounds.northeast.latitude;
        double endLatitude = latLngBounds.southwest.latitude;

        double startLongitude = latLngBounds.southwest.longitude;
        double endLongitude = latLngBounds.northeast.longitude;

        double stepLatitude = 180.0 / tileCount;
        double stepLongitude = 360.0 / tileCount;

        long startX = (long) ((startLongitude + 180.0) / stepLongitude);
        long startY = (long) ((90.0 - startLatitude) / stepLatitude);

        long endX = (long) ((endLongitude + 180.0) / stepLongitude) + 1;
        long endY = (long) ((90.0 - endLatitude) / stepLatitude) + 1;

        for (long tileX = startX; tileX <= endX; tileX++) {
            for (long tileY = startY; tileY <= endY; tileY++) {
                double north = 90.0 - tileY * stepLatitude;
                double west = tileX * stepLongitude - 180.0;
                double south = north - stepLatitude;
                double east = west + stepLongitude;

                List<T> points = mQuadTree.queryRange(north, west, south, east);

                if (points.size() > 0) {
                    double totalLatitude = 0;
                    double totalLongitude = 0;

                    for (QuadTreePoint point : points) {
                        totalLatitude += point.getLatitude();
                        totalLongitude += point.getLongitude();
                    }

                    double latitude = totalLatitude / points.size();
                    double longitude = totalLongitude / points.size();

                    clusters.add(new Cluster<>(latitude, longitude, points,
                            north, west, south, east));
                }
            }
        }

        return clusters;
    }

    private class QuadTreeTask extends AsyncTask<Void, Void, Void> {

        private final List<T> mClusterItems;

        private QuadTreeTask(@NonNull List<T> clusterItems) {
            mClusterItems = clusterItems;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mQuadTree.clear();
            for (T clusterItem : mClusterItems) {
                mQuadTree.insert(clusterItem);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            cluster();
            mQuadTreeTask = null;
        }
    }

    private class ClusterTask extends AsyncTask<Void, Void, List<Cluster<T>>> {

        private final LatLngBounds mLatLngBounds;
        private final float mZoomLevel;

        private ClusterTask(@NonNull LatLngBounds latLngBounds, float zoomLevel) {
            mLatLngBounds = latLngBounds;
            mZoomLevel = zoomLevel;
        }

        @Override
        protected List<Cluster<T>> doInBackground(Void... params) {
            return getClusters(mLatLngBounds, mZoomLevel);
        }

        @Override
        protected void onPostExecute(@NonNull List<Cluster<T>> clusters) {
            mRenderer.render(clusters);
            mClusterTask = null;
        }
    }
}
