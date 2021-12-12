package ch.heigvd.sym.sym_labo3

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*

/**
 * Activity to live scan barcodes & QR codes
 *
 * Inspired by the lib example:
 * https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/ContinuousCaptureActivity.java
 */
class QRActivity : AppCompatActivity() {

    private lateinit var scanImage: ImageView
    private lateinit var scanText: TextView
    private lateinit var barcodeView: DecoratedBarcodeView

    // Ask for camera access permission
    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            if (!isGranted) {
                closeOnNotGranted()
            }
        }

    // Behaviour if camera access is not granted
    private fun closeOnNotGranted () {
        Toast.makeText(this, R.string.must_access_camera, Toast.LENGTH_SHORT).show()
        finish()
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
            // Prevent scanning the same barcode twice in a row
            if (result.text == null || result.text == scanText.text) return

            // Update result preview & show scanning points on picture
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