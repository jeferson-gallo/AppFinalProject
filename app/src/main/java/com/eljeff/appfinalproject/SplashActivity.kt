package com.eljeff.appfinalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        val timer  = Timer()

        timer.schedule(timerTask {
            goToLoginActivity()
        }, 1000
        ) // SE cierra schedule
    }

    private fun goToLoginActivity() {

        val intent = Intent(this, LoginActivity::class.java)

        //Creamos un usuario vacio para mandar
        val user = User()
        intent.putExtra("user", user)

        // Limpiamos la pila de actividades
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        //Se llama la actividad de login
        startActivity(intent)
    }
}