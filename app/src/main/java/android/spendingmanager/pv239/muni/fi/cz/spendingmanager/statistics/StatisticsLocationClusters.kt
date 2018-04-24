package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.statistics

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem


class StatisticsClusterItem : ClusterItem {
    private val mPosition: LatLng
    private val mTitle: String
    private val mSnippet: String
    private val mValue: Int

    constructor(lat: Double, lng: Double, value: Int) {
        mPosition = LatLng(lat, lng)
        mTitle = ""
        mSnippet = ""
        mValue = value
    }

    constructor(lat: Double, lng: Double, title: String, snippet: String, value: Int) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mValue = value
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    fun getValue(): Int {
        return mValue
    }
}