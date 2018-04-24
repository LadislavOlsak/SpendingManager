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
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.general.ViewMode
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.snatik.storage.Storage
import kotlinx.android.synthetic.main.loyalty_card_item.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_loyalty_card)

        setHandlers()

        initView()
        setCardNumber()

        //todo handle mode : New / Edit
    }

    private fun getColoredBackground(color : Int) : Drawable {
        val border = getBorderDrawable()
        border.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        return border
    }

    override fun colorPicked(color: Int?) {
        if(color != null) {
            cardColor = color
            new_loyalty_card_preview_layout.background = getColoredBackground(color)
        }
    }

    private fun getBorderDrawable(): Drawable {
        return ContextCompat.getDrawable(this, R.drawable.customborder) as Drawable
    }

    private fun setHandlers() {
        new_loyalty_card_snap_front.setOnClickListener { takePicture(true) }
        new_loyalty_card_snap_back.setOnClickListener { takePicture(false) }
        new_loyalty_card_picked_color_iv.setOnClickListener {
            ColorPickerFragment().showDialog(this, supportFragmentManager)
        }
        new_loyalty_card_name.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setPreviewCardName(p0.toString())
            }
        })
        new_loyalty_card_number.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                setPreviewCardBarcode(p0.toString())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
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

    private fun initView() {
        loyalty_cards_item_edit_iv.visibility = View.GONE
        loyalty_cards_item_delete_iv.visibility = View.GONE

        val viewMode = ViewMode.valueOf(intent.getStringExtra("mode"))

        when(viewMode) {
            ViewMode.New -> setCardNumber()
            ViewMode.Edit -> setCardDetails()
            else -> { }
        }
    }

    private fun setCardNumber() {
        cardNumber = intent.getStringExtra("cardNumber")
        if(cardNumber != null && cardNumber?.isNotEmpty() == true) {
            new_loyalty_card_number.setText(cardNumber)
        }
    }

    private fun setCardDetails() {
        val loyaltyCard = Gson().fromJson(intent.getStringExtra("card"), LoyaltyCard::class.java)
        new_loyalty_card_name.setText(loyaltyCard.cardName)
        new_loyalty_card_number.setText(loyaltyCard.cardNumber)
        setPreviewCardName(loyaltyCard.cardName)
        setPreviewCardBarcode(loyaltyCard.cardNumber)

        colorPicked(loyaltyCard.color)
        //TODO set File paths / Images
    }

    private fun setPreviewCardName(cardName : String) {
        loyalty_cards_item_name_tv.text = cardName
    }

    private fun setPreviewCardBarcode(cardNumber : String) {
        loyalty_card_item_barcode_iv.setImageBitmap(BarcodeImageGenerator.getBarcodeBitmap(cardNumber))
    }
}
