package com.eljeff.appfinalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        //val data = intent.extras
        //val user : User = intent.extras?.getSerializable("user") as User

        val user : User = intent.extras?.getSerializable("user") as User
        mainBinding.showEmailTxVw.text = user.email

        Log.d("metodo", "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout_menu -> {
                // Opción 2
                //onBackPressed()

                // Opción 1
                val intent = Intent(this, LoginActivity::class.java)

                // Limpiamos la pila de actividades
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                // Se llama a la actividad
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        Log.d("metodo", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("metodo", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("metodo", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("metodo", "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("metodo", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("metodo", "onDestroy")
    }
}