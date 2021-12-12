package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseNFCActivity : AppCompatActivity() {

    private lateinit var nfcModule: NfcModule
    private var nfcAdapter: NfcAdapter? = null
    protected val wantedPayload = "1 2 3 4"

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the NFC module
        nfcModule = NfcModule(this)
        nfcModule.init()
        nfcModule.setTokenListener(object: TokenEventListener {
            override fun handleToken(token: Token) {
                onTokenBehaviour(token)
            }
        })
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
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

    /**
     * Specify the behaviour on NFC detection
     */
    abstract fun onTokenBehaviour(token: Token)
}