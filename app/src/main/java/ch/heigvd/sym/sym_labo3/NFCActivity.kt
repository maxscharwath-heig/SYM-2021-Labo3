package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import java.time.Duration
import java.time.LocalDateTime

class NFCActivity : AppCompatActivity() {

    companion object {
        private enum class SecurityLevel(val duration: Int) {
            HIGH(10),
            MEDIUM(30),
            LOW(60);
        }
    }

    private lateinit var nfcModule: NfcModule
    private lateinit var max_button : Button
    private lateinit var medium_button : Button
    private lateinit var low_button : Button
    private var nfcAdapter: NfcAdapter? = null
    private var lastNFCTime : LocalDateTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        nfcModule =  NfcModule(this);
        nfcModule.init()
        nfcModule.setTokenListener(object:TokenEventListener{
            override fun handleToken(token: Token) {
                Toast.makeText(this@NFCActivity,token.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        max_button = findViewById(R.id.btn_max_secu)
        medium_button = findViewById(R.id.btn_med_secu)
        low_button = findViewById(R.id.btn_low_secu)

        max_button.setOnClickListener {
            if (Duration.between(lastNFCTime, LocalDateTime.now()).seconds <= SecurityLevel.HIGH.duration) {
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Too late !", Toast.LENGTH_SHORT).show()
            }
        }

        lastNFCTime = LocalDateTime.now()
    }

    override fun onResume() {
        super.onResume()
        nfcModule.start()
    }

    override fun onPause() {
        super.onPause()
        nfcModule.stop()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        nfcModule.onIntent(intent)
    }
}