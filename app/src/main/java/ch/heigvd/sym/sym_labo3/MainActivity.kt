package ch.heigvd.sym.sym_labo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var barcodeActivityBtn : Button
    private lateinit var beaconActivityBtn : Button
    private lateinit var NFCActivityBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barcodeActivityBtn = findViewById(R.id.btn_qr_launch)
        beaconActivityBtn = findViewById(R.id.btn_beacon_launch)
        NFCActivityBtn = findViewById(R.id.btn_nfc_launch)

        barcodeActivityBtn.setOnClickListener {
            val intent = Intent(this, QRActivity::class.java)
            startActivity(intent)

            return@setOnClickListener
        }

        beaconActivityBtn.setOnClickListener {
            val intent = Intent(this, BeaconActivity::class.java)
            startActivity(intent)

            return@setOnClickListener
        }

        NFCActivityBtn.setOnClickListener {
            val intent = Intent(this, NFCActivity::class.java)
            startActivity(intent)

            return@setOnClickListener
        }
    }
}