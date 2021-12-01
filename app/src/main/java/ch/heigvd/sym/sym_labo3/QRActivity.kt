package ch.heigvd.sym.sym_labo3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*

/**
 * Inspired by the lib example:
 * https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/ContinuousCaptureActivity.java
 */

class QRActivity : AppCompatActivity() {

    private lateinit var scanImage: ImageView
    private lateinit var scanText: TextView
    private lateinit var barcodeView: DecoratedBarcodeView

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        cameraPermission.launch(Manifest.permission.CAMERA)

        scanImage = findViewById(R.id.barcodeResultImg)
        scanText = findViewById(R.id.barcodeNumber)
        barcodeView = findViewById(R.id.barcode_embed)

        val formats: Collection<BarcodeFormat> =
            listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(intent)
        barcodeView.decodeContinuous(barcodeCallback)
    }

    private val barcodeCallback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == scanText.text) return
            // TODO: Check if multiple in a row is working
            scanText.text = result.text
            scanImage.setImageBitmap(result.getBitmapWithResultPoints(Color.RED))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}