# Google Maps Clustering for Android

[ ![Download](https://api.bintray.com/packages/sharewire/maven/google-maps-clustering/images/download.svg) ](https://bintray.com/sharewire/maven/google-maps-clustering/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Google%20Maps%20Clustering%20for%20Android-blue.svg?style=flat)](https://android-arsenal.com/details/1/6552) <a href="http://www.methodscount.com/?lib=net.sharewire%3Agoogle-maps-clustering%3A0.1.2-beta"><img src="https://img.shields.io/badge/Methods count-193-e91e63.svg"/></a>

A fast marker clustering library for Google Maps Android API.

![Demo](art/demo.gif)

## Motivation
Why not use [Google Maps Android API Utility Library](https://github.com/googlemaps/android-maps-utils)? Because it's very slow for large amounts of markers, which causes skipping frames and ANRs (see [Issue #29](https://github.com/googlemaps/android-maps-utils/issues/29), [Issue #82](https://github.com/googlemaps/android-maps-utils/issues/82)). But this library can easily handle thousands of markers (the video above demonstrates the sample application with 20 000 markers running on Nexus 5).

## Installation
1. Make sure you have JCenter in your repository list:
```groovy
repositories {
    jcenter()
}
```
2. Add a dependency to your build.gradle:
```groovy
dependencies {
    compile 'net.sharewire:google-maps-clustering:0.1.3'
}
```

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
