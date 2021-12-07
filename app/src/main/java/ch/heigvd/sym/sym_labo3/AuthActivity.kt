package ch.heigvd.sym.sym_labo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AuthActivity : AppCompatActivity() {

    companion object {
        private val crendentials = arrayListOf(Pair("test@test.ch", "1234"))
    }

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var submit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        emailInput = findViewById(R.id.login_email_input)
        passwordInput = findViewById(R.id.login_pwd_input)
        submit = findViewById(R.id.login_btn)

        submit.setOnClickListener {

            if (!crendentials.contains(Pair(emailInput.text?.toString(), passwordInput.text?.toString()))) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, NFCActivity::class.java)
            startActivity(intent)

            return@setOnClickListener
        }
    }
}