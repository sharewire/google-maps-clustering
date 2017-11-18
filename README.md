# Google Maps Clustering for Android

A fast marker clustering library for Google Maps Android API. 

## Integration
1. Implement `ClusterItem` to represent a marker on the map. The cluster item returns the position of the marker and an optional title or snippet.
2. Create an instance of ClusterManager and set it as a camera idle listener using `GoogleMap.setOnCameraIdleListener(...)`.
3. To add a callback that's invoked when a cluster or a cluster item is clicked, use `ClusterManager.setCallbacks(...)`.
4. To customize the icons create an instance of `IconGenerator` and set it using `ClusterManager.setIconGenerator(...)`. You can also use the default implementation `DefaultIconGenerator` and customize the style of icons using `DefaultIconGenerator.setIconStyle(...)`.
5. Populate ClusterManager with items using `ClusterManager.setItems(...)`.
