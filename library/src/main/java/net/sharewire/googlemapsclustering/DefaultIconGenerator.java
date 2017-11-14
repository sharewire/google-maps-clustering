package net.sharewire.googlemapsclustering;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import static net.sharewire.googlemapsclustering.Preconditions.checkNotNull;

public class DefaultIconGenerator<T extends ClusterItem> implements IconGenerator<T> {

    private static final int[] CLUSTER_ICON_BUCKETS = {10, 20, 50, 100, 500, 1000, 5000, 10000, 20000};

    private final Context mContext;

    private IconStyle mIconStyle;

    private final SparseArray<BitmapDescriptor> mClusterIcons = new SparseArray<>();

    private BitmapDescriptor mClusterItemIcon;

    public DefaultIconGenerator(@NonNull Context context) {
        mContext = checkNotNull(context);
        setIconStyle(createDefaultIconStyle());
    }

    public void setIconStyle(@NonNull IconStyle iconStyle) {
        mIconStyle = checkNotNull(iconStyle);
    }

    @NonNull
    public BitmapDescriptor getClusterIcon(@NonNull Cluster<T> cluster) {
        int clusterBucket = getClusterIconBucket(cluster);
        BitmapDescriptor clusterIcon = mClusterIcons.get(clusterBucket);

        if (clusterIcon == null) {
            clusterIcon = createClusterIcon(clusterBucket);
            mClusterIcons.put(clusterBucket, clusterIcon);
        }

        return clusterIcon;
    }

    @NonNull
    @Override
    public BitmapDescriptor getClusterItemIcon(@NonNull T clusterItem) {
        if (mClusterItemIcon == null) {
            mClusterItemIcon = createClusterItemIcon();
        }
        return mClusterItemIcon;
    }

    @NonNull
    private IconStyle createDefaultIconStyle() {
        return new IconStyle.Builder(mContext).build();
    }

    @NonNull
    private BitmapDescriptor createClusterIcon(int clusterBucket) {
        @SuppressLint("InflateParams")
        TextView clusterIconView = (TextView) LayoutInflater.from(mContext)
                .inflate(R.layout.map_cluster_icon, null);
        clusterIconView.setBackground(createClusterBackground());
        clusterIconView.setTextColor(mIconStyle.getClusterTextColor());
        clusterIconView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mIconStyle.getClusterTextSize());

        clusterIconView.setText(getClusterIconText(clusterBucket));

        clusterIconView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        clusterIconView.layout(0, 0, clusterIconView.getMeasuredWidth(),
                clusterIconView.getMeasuredHeight());

        Bitmap iconBitmap = Bitmap.createBitmap(clusterIconView.getMeasuredWidth(),
                clusterIconView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(iconBitmap);
        clusterIconView.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(iconBitmap);
    }

    @NonNull
    private Drawable createClusterBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(mIconStyle.getClusterBackgroundColor());
        gradientDrawable.setStroke(mIconStyle.getClusterStrokeWidth(),
                mIconStyle.getClusterStrokeColor());
        return gradientDrawable;
    }

    @NonNull
    private BitmapDescriptor createClusterItemIcon() {
        return BitmapDescriptorFactory.fromResource(mIconStyle.getClusterIconResId());
    }

    private int getClusterIconBucket(@NonNull Cluster<T> cluster) {
        int itemCount = cluster.getItems().size();
        if (itemCount <= CLUSTER_ICON_BUCKETS[0]) {
            return itemCount;
        }

        for (int i = 0; i < CLUSTER_ICON_BUCKETS.length - 1; i++) {
            if (itemCount < CLUSTER_ICON_BUCKETS[i + 1]) {
                return CLUSTER_ICON_BUCKETS[i];
            }
        }

        return CLUSTER_ICON_BUCKETS[CLUSTER_ICON_BUCKETS.length - 1];
    }

    @NonNull
    private String getClusterIconText(int clusterIconBucket) {
        return (clusterIconBucket < CLUSTER_ICON_BUCKETS[0]) ?
                String.valueOf(clusterIconBucket) : String.valueOf(clusterIconBucket) + "+";
    }
}
