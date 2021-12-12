package ch.heigvd.sym.sym_labo3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class AuthActivity : BaseNFCActivity() {

    companion object {
        private val crendentials = arrayListOf(Pair("test@test.ch", "1234"))
    }

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var submit: Button
    private lateinit var rdNFCGranted: CheckBox
    private var isNFCAuthOk: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        emailInput = findViewById(R.id.login_email_input)
        passwordInput = findViewById(R.id.login_pwd_input)
        rdNFCGranted = findViewById(R.id.nfc_granted)
        submit = findViewById(R.id.login_btn)

        emailInput.setText("test@test.ch")

        submit.setOnClickListener {
            // If credentials are wrong, reset NFC access
            if (!crendentials.contains(Pair(emailInput.text?.toString(), passwordInput.text?.toString()))) {
                Toast.makeText(this, getString(R.string.invalid_creds), Toast.LENGTH_SHORT).show()
                toggleNFCAuthConfirm(false)
                return@setOnClickListener
            }

            // Check for NFC
            if (!isNFCAuthOk) {
                Toast.makeText(this, getString(R.string.should_scan_tag), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Launch secured activity
            val intent = Intent(this, NFCActivity::class.java)
            startActivity(intent)

            return@setOnClickListener
        }
    }

    override fun onTokenBehaviour(token: Token) {
        if (token.payload == wantedPayload) {
            toggleNFCAuthConfirm(true)
        }
    }

    private fun toggleNFCAuthConfirm (state: Boolean) {
        isNFCAuthOk = state
        rdNFCGranted.isChecked = state
    }
}