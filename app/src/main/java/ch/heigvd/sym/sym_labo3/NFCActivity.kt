package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.PendingIntent
import android.content.IntentFilter
import android.nfc.NfcAdapter.ACTION_TECH_DISCOVERED
import android.nfc.Tag
import android.nfc.tech.IsoDep


class NFCActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }
        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val launchIntent = Intent(this, this.javaClass)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        val filters = arrayOf(IntentFilter(ACTION_TECH_DISCOVERED))
        val techTypes = arrayOf(arrayOf(IsoDep::class.java.name))

        nfcAdapter!!.enableForegroundDispatch(
            this, pendingIntent, filters, techTypes
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var action = intent?.action
        if(action == null) action = "nothing"
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show()
        if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {
            val tag = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            IsoDep.get(tag)?.let { isoDepTag ->
                Toast.makeText(this,isoDepTag.tag.id.toString(),Toast.LENGTH_SHORT)
            }
        }
    }
}