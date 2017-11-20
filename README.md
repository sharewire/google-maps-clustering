# Google Maps Clustering for Android

A fast marker clustering library for Google Maps Android API. 

![Demo](art/demo.gif)

## Integration
1. Implement `ClusterItem` to represent a marker on the map. The cluster item returns the position of the marker and an optional title or snippet:

```java

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
}
```

2. Create an instance of ClusterManager and set it as a camera idle listener using `GoogleMap.setOnCameraIdleListener(...)`:

```java
ClusterManager<SampleClusterItem> clusterManager = new ClusterManager<>(context, googleMap);
googleMap.setOnCameraIdleListener(clusterManager);
```

3. To add a callback that's invoked when a cluster or a cluster item is clicked, use `ClusterManager.setCallbacks(...)`:

```java
clusterManager.setCallbacks(new ClusterManager.Callbacks<SampleClusterItem>() {
            @Override
            public boolean onClusterClick(@NonNull Cluster<SampleClusterItem> cluster) {
                Log.d(TAG, "onClusterClick");
                return false;
            }

            @Override
            public boolean onClusterItemClick(@NonNull SampleClusterItem clusterItem) {
                Log.d(TAG, "onClusterItemClick");
                return false;
            }
        });
```

4. To customize the icons create an instance of `IconGenerator` and set it using `ClusterManager.setIconGenerator(...)`. You can also use the default implementation `DefaultIconGenerator` and customize the style of icons using `DefaultIconGenerator.setIconStyle(...)`.

5. Populate ClusterManager with items using `ClusterManager.setItems(...)`:

```java
List<SampleClusterItem> clusterItems = generateSampleClusterItems();
clusterManager.setItems(clusterItems);
```
