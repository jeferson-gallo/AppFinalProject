package com.eljeff.appfinalproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        registerBinding.registerButton.setOnClickListener {

            val user = User(
                userName = registerBinding.userNameRegEdTx.text.toString(),
                email = registerBinding.emailRegEdTx.text.toString(),
                password = registerBinding.passwordRegEdTx.text.toString()
            )

            // Contenedor para tranferir datos entre actividades
            val intent = Intent(this, LoginActivity::class.java)

            // Mandar datos a otra actividad
            intent.putExtra("user", user)

            // Limpiamos la pila de actividades
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )

            // LLamar actividad de login
            startActivity(intent)
        }

    }
}