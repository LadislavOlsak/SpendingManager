package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlin.math.roundToInt


class StatisticsClusterManager(context: Context, map: GoogleMap, clusterManager: ClusterManager<StatisticsClusterItem>, getValue: Boolean)
    : DefaultClusterRenderer<StatisticsClusterItem>(context, map, clusterManager)
{
    private val getValue = getValue
    private var value : Double = 0.0

    override fun getBucket(cluster: Cluster<StatisticsClusterItem>): Int {
        if (getValue)
        {
            value = cluster.items.sumByDouble { item -> item.getValue() }
            return value.toInt()
        }
        else
        {
            value = cluster.size.toDouble()
            return value.toInt()
        }
    }

    override fun getClusterText(bucket: Int): String {
        if (getValue)
        {
            return value.toString()
        }
        else
        {
            return value.toInt().toString()
        }

    }

    override fun shouldRenderAsCluster(cluster: Cluster<StatisticsClusterItem>): Boolean {
        return cluster.size > 1
    }
}