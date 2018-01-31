package net.sharewire.googlemapsclustering;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static net.sharewire.googlemapsclustering.Preconditions.checkNotNull;

/**
 * Groups multiple items on a map into clusters based on the current zoom level.
 * Clustering occurs when the map becomes idle, so an instance of this class
 * must be set as a camera idle listener using {@link GoogleMap#setOnCameraIdleListener}.
 *
 * @param <T> the type of an item to be clustered
 */
public class ClusterManager<T extends ClusterItem> implements GoogleMap.OnCameraIdleListener {

    private static final int QUAD_TREE_BUCKET_CAPACITY = 4;

    private final GoogleMap mGoogleMap;

    private final QuadTree<T> mQuadTree;

    private final ClusterRenderer<T> mRenderer;

    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    private AsyncTask mQuadTreeTask;

    private AsyncTask mClusterTask;

    /**
     * Defines signatures for methods that are called when a cluster or a cluster item is clicked.
     *
     * @param <T> the type of an item managed by {@link ClusterManager}.
     */
    public interface Callbacks<T extends ClusterItem> {
        /**
         * Called when a marker representing a cluster has been clicked.
         *
         * @param cluster the cluster that has been clicked
         * @return <code>true</code> if the listener has consumed the event (i.e., the default behavior should not occur);
         * <code>false</code> otherwise (i.e., the default behavior should occur). The default behavior is for the camera
         * to move to the marker and an info window to appear.
         */
        boolean onClusterClick(@NonNull Cluster<T> cluster);

        /**
         * Called when a marker representing a cluster item has been clicked.
         *
         * @param clusterItem the cluster item that has been clicked
         * @return <code>true</code> if the listener has consumed the event (i.e., the default behavior should not occur);
         * <code>false</code> otherwise (i.e., the default behavior should occur). The default behavior is for the camera
         * to move to the marker and an info window to appear.
         */
        boolean onClusterItemClick(@NonNull T clusterItem);
    }

    /**
     * Creates a new cluster manager using the default icon generator.
     * To customize marker icons, set a custom icon generator using
     * {@link ClusterManager#setIconGenerator(IconGenerator)}.
     *
     * @param googleMap the map instance where markers will be rendered
     */
    public ClusterManager(
            @NonNull Context context,
            @NonNull GoogleMap googleMap,
            @NonNull ClusterRenderer<T> clusterRenderer
    ) {
        checkNotNull(context);
        mGoogleMap = checkNotNull(googleMap);
        mRenderer = checkNotNull(clusterRenderer);
        mQuadTree = new QuadTree<>(QUAD_TREE_BUCKET_CAPACITY);
    }

    /**
     * Sets a custom icon generator thus replacing the default one.
     *
     * @param iconGenerator the custom icon generator that's used for generating marker icons
     */
    public void setIconGenerator(@NonNull IconGenerator<T> iconGenerator) {
        checkNotNull(iconGenerator);
        mRenderer.setIconGenerator(iconGenerator);
    }

    /**
     * Sets a callback that's invoked when a cluster or a cluster item is clicked.
     *
     * @param callbacks the callback that's invoked when a cluster or an individual item is clicked.
     *                  To unset the callback, use <code>null</code>.
     */
    public void setCallbacks(@Nullable Callbacks<T> callbacks) {
        mRenderer.setCallbacks(callbacks);
    }

    /**
     * Sets items to be clustered thus replacing the old ones.
     *
     * @param clusterItems the items to be clustered
     */
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

        mQuadTreeTask = new QuadTreeTask(clusterItems).executeOnExecutor(mExecutor);
    }

    private void cluster() {
        if (mClusterTask != null) {
            mClusterTask.cancel(true);
        }

        mClusterTask = new ClusterTask(mGoogleMap.getProjection().getVisibleRegion().latLngBounds,
                mGoogleMap.getCameraPosition().zoom).executeOnExecutor(mExecutor);
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
