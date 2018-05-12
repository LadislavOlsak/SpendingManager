package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location

import org.ankit.gpslibrary.MyTracker

class PoiPlace {

    constructor()

    constructor(name : String, address : String, latitude : Double, longitude : Double) {
        this.name = name
        this.address = address
        this.longitude = longitude
        this.latitude = latitude
    }

    constructor(tracker : MyTracker) {
        this.name = tracker.address
        this.address = tracker.address
        this.latitude = tracker.latitude
        this.longitude = tracker.longitude
    }

    var name : String = ""
    var address : String = ""
    var latitude : Double = Double.NaN
    var longitude : Double = Double.NaN
}