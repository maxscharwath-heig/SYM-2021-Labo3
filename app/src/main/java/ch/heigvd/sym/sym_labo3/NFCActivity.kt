package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NFCActivity : AppCompatActivity() {


    private lateinit var nfcModule: NfcModule

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