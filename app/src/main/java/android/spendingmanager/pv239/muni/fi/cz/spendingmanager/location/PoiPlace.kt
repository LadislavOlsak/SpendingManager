package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.location

class PoiPlace {

    constructor()

    constructor(name : String, address : String, latitude : Double, longitude : Double) {
        this.name = name
        this.address = address
        this.longitude = longitude
        this.latitude = latitude
    }

    var name : String = ""
    var address : String = ""
    var latitude : Double = Double.NaN
    var longitude : Double = Double.NaN
}