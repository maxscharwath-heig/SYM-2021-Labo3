package ch.heigvd.sym.sym_labo3

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanOptions
import android.widget.Toast
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult


class QRActivity : AppCompatActivity() {

    private lateinit var scanBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        scanBtn = findViewById(R.id.scan_btn)

        scanBtn.setOnClickListener {
            val options = ScanOptions()
            options.setOrientationLocked(false)
            barcodeLauncher.launch(options)
            return@setOnClickListener
        }
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@QRActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@QRActivity,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}