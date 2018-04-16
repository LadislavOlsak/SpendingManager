package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general

import android.support.v7.app.AppCompatActivity

abstract class ColorPickingActivity : AppCompatActivity() {
    abstract fun colorPicked(color : Int?)
}