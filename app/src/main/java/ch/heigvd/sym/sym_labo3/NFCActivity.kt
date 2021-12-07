package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter.ACTION_TECH_DISCOVERED
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import java.nio.charset.Charset
import kotlin.experimental.and

data class Token(val mime:String, val languageCode:String, val payload:String){
    companion object {
        fun fromRecord(record: NdefRecord):Token{
            val payloadBytes: ByteArray = record.payload
            val isUTF8: Boolean = (payloadBytes[0] and 0x080.toByte()).toInt() == 0
            val languageLength: Int = (payloadBytes[0] and 0x03F).toInt()
            val textLength = payloadBytes.size - 1 - languageLength
            val languageCode = String(payloadBytes, 1, languageLength, Charset.forName("US-ASCII"))
            val payloadText = String(
                payloadBytes,
                1 + languageLength,
                textLength,
                Charset.forName(if (isUTF8) "UTF-8" else "UTF-16")
            )
            return Token(record.toMimeType(), languageCode, payloadText)
        }
    }
}

class NFCActivity : AppCompatActivity() {


    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)



        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return

        }
        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupForegroundDispatch() {
        if (nfcAdapter == null) return
        val intent = Intent(this.applicationContext, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            Log.e(TAG, "MalformedMimeTypeException", e)
        }
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    // called in onPause()
    private fun stopForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch();
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                for (message in messages) {
                    for (record in message.records) {
                        val token = Token.fromRecord(record);
                        Toast.makeText(this,token.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}