package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general

import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.support.v4.app.FragmentManager
import com.thebluealliance.spectrum.SpectrumDialog

class ColorPickerFragment {

    fun showDialog(pContext : ColorPickingActivity, frMan : FragmentManager) : SpectrumDialog {
        val dialog = SpectrumDialog.Builder(pContext)
            .setColors(R.array.demo_colors)
            .setSelectedColorRes(R.color.md_blue_500)
            .setDismissOnColorSelected(true)
            .setOutlineWidth(2)
            .setOnColorSelectedListener { _, color ->
                pContext.colorPicked(color)
            }.build()
        dialog.show(frMan, "color_picker_dialog")

        return dialog
    }

}