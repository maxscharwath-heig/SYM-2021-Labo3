package ch.heigvd.sym.sym_labo3

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanOptions
import android.widget.Toast
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QRActivity : AppCompatActivity() {

    private lateinit var scanBtn : Button
    private lateinit var scanImage: ImageView
    private lateinit var scanText: TextView
    private lateinit var barcodeView: DecoratedBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Need to embed the scanner in the activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        scanBtn = findViewById(R.id.scan_btn)
        scanImage = findViewById(R.id.barcodeResultImg)
        scanText = findViewById(R.id.barcodeNumber)
        // barcodeView = findViewById(R.id.barcodeCompound)

        scanBtn.setOnClickListener {
            val options = ScanOptions()
            options.setOrientationLocked(false)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
            return@setOnClickListener
        }
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@QRActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            scanText.text = result.contents
            val bmImg = BitmapFactory.decodeFile(result.barcodeImagePath)
            scanImage.setImageBitmap(bmImg)
        }
    }
}