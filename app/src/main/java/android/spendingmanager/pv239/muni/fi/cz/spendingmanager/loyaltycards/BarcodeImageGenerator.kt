package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class BarcodeImageGenerator {

    companion object {
        fun getBarcodeBitmap(cardNumber : String) : Bitmap? {
            try {
                val bitMatrix = MultiFormatWriter().encode(cardNumber, BarcodeFormat.CODE_128,600,200) as BitMatrix
                return BarcodeEncoder().createBitmap(bitMatrix)
            } catch (e : WriterException) {
                e.printStackTrace()
            }
            return null
        }
    }
}