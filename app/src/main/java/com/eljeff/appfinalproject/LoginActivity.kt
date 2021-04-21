package com.eljeff.appfinalproject

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // Cuando se llama el boton de loguearse
        loginBinding.loginButton.setOnClickListener {

            val user = User(email = loginBinding.userEmailLogEdTx.text.toString())

            // Contenedor para tranferir datos entre actividades
            val intent = Intent(this, MainActivity::class.java)

            // Mandar datos a otra actividad
            intent.putExtra("user", user)

            // Limpiamos la pila de actividades
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)

            // LLamar actividad de login
            startActivity(intent)
        }

        // Cuando se llama El edit text para Registrarse
        loginBinding.loginRegister.setOnClickListener {

            // Contenedor para tranferir datos entre actividades
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }

    }
}