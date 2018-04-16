package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.loyaltycards

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_loyalty_card.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ColorPickingActivity
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ColorPickerFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import com.snatik.storage.Storage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NewLoyaltyCardActivity (
        private var cardNumber : String? = null,
        private val REQUEST_IMAGE_CAPTURE : Int = 1,
        private var isFrontPicture : Boolean? = null
) : ColorPickingActivity()  {

    private var cardColor : Int? = null

    override fun colorPicked(color: Int?) {
        if(color != null) {
            cardColor = color
            new_loyalty_card_picked_color_iv.background = getColoredBackground(color)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_loyalty_card)

        setHandlers()
        setCardNumber()

    }

    private fun getColoredBackground(color : Int) : Drawable {
        val border = getBorderDrawable()
        border.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        return border
    }

    private fun getBorderDrawable(): Drawable {
        return ContextCompat.getDrawable(this, R.drawable.customborder)
    }

    private fun setHandlers() {
        new_loyalty_card_snap_front.setOnClickListener { takePicture(true) }
        new_loyalty_card_snap_back.setOnClickListener { takePicture(false) }
        new_loyalty_card_picked_color_iv.setOnClickListener {
            ColorPickerFragment().showDialog(this, supportFragmentManager)
        }
    }

    private fun takePicture(isFront : Boolean) {
        isFrontPicture = isFront
        dispatchTakePictureIntent()
    }

    private fun getCorrectImageView() : ImageView {
        return if (isFrontPicture == true) new_loyalty_card_snap_front else new_loyalty_card_snap_back
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            getCorrectImageView().setImageBitmap(imageBitmap)
        }
    }

    private fun createImageFile() : String? {
        val storage = Storage(this)
        val path = storage.externalStorageDirectory + File.separator + "SpendingManager" + File.separator + "LoyaltyCards"

        if(!storage.isDirectoryExists(path)) {
            storage.createDirectory(path)
        }

        val filePath = getFilePath(path)
        val isFileCreated = storage.createFile(filePath, ByteArray(0))

        return if(isFileCreated) filePath else null
    }

    private fun getFilePath(dirPath: String) : String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_" + ".jpg"
        return dirPath + File.separator + imageFileName
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoURI : String? = null
            try {
                photoURI = createImageFile()
            } catch (ex : IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
            }
            if (photoURI != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun setCardNumber() {
        cardNumber = intent.getStringExtra("cardNumber")
        if(cardNumber != null && cardNumber?.isNotEmpty() == true) {
            new_loyalty_card_number.setText(cardNumber)

        }
    }

    //TODO:
//    private fun inflatePreview() {
//        LayoutInflater.from(this).inflate(R.layout.loyalty_card_item, window.decorView, false)
//    }

}
