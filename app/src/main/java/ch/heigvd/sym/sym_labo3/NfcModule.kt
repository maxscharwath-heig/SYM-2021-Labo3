package ch.heigvd.sym.sym_labo3

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset
import kotlin.experimental.and

interface TokenEventListener {
    fun handleToken(token: Token)
}

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

class NfcModule(val activity: AppCompatActivity) {
    private var tokenEventListener: TokenEventListener? = null
    private var nfcAdapter: NfcAdapter? = null
    fun init(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

        if (nfcAdapter == null) {
            Toast.makeText(activity, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return

        }
        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                activity,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun start(){
        if (nfcAdapter == null) return
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()
        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            Log.e(ContentValues.TAG, "MalformedMimeTypeException", e)
        }
        nfcAdapter!!.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    fun stop(){
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    fun onIntent(intent: Intent?) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                for (message in messages) {
                    for (record in message.records) {
                        tokenEventListener?.handleToken(Token.fromRecord(record));
                    }
                }
            }
        }

    }

    fun setTokenListener(tokenEventListener: TokenEventListener) {
        this.tokenEventListener = tokenEventListener
    }
}