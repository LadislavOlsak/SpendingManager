package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager

class StatisticsClusterManager(context: Context, map: GoogleMap, clusterManager: ClusterManager<StatisticsClusterItem>, getValue: Boolean)
    : DefaultClusterRenderer<StatisticsClusterItem>(context, map, clusterManager)
{
    private val getValue = getValue
    private var value : Int = 0

    override fun getBucket(cluster: Cluster<StatisticsClusterItem>): Int {
        if (getValue)
        {
            value = cluster.items.sumBy { item -> item.getValue() }
            return value
        }
        else
        {
            value = cluster.size
            return value
        }
    }

    override fun getClusterText(bucket: Int): String {
        return value.toString()
    }
}