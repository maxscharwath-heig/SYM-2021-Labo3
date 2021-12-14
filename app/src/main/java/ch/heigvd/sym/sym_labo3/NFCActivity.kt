package ch.heigvd.sym.sym_labo3

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.time.Duration
import java.time.LocalDateTime

class NFCActivity : BaseNFCActivity() {

    companion object {
        private enum class SecurityLevel(val duration: Int) {
            HIGH(10),
            MEDIUM(30),
            LOW(60);
        }
    }

    private lateinit var maxButton: Button
    private lateinit var mediumButton: Button
    private lateinit var lowButton: Button
    private var lastNFCTime: LocalDateTime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        maxButton = findViewById(R.id.btn_max_secu)
        mediumButton = findViewById(R.id.btn_med_secu)
        lowButton = findViewById(R.id.btn_low_secu)

        maxButton.setOnClickListener { secureBehaviour(SecurityLevel.HIGH) }
        mediumButton.setOnClickListener { secureBehaviour(SecurityLevel.MEDIUM) }
        lowButton.setOnClickListener { secureBehaviour(SecurityLevel.LOW) }

        lastNFCTime = LocalDateTime.now()
    }

    private fun secureBehaviour(level: SecurityLevel) {
        if (Duration.between(lastNFCTime, LocalDateTime.now()).seconds <= level.duration) {
            Toast.makeText(
                this@NFCActivity,
                getString(R.string.security_granted),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@NFCActivity,
                getString(R.string.security_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onTokenBehaviour(token: Token) {
        if (token.payload == wantedPayload) {
            lastNFCTime = LocalDateTime.now()
            Toast.makeText(this@NFCActivity, getString(R.string.access_renewed), Toast.LENGTH_SHORT)
                .show()
        }
    }
}