package com.eljeff.appfinalproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {
    //develop


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_purchase, R.id.nav_cart, R.id.nav_chat, R.id.nav_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    // agregamos el menú overflow a el appbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // cuando se selecciona un item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // cerrar sesión
        if(item.itemId == R.id.logout_menu){
            Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show()
            goToLoginActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToLoginActivity() {
        // Contenedor para tranferir datos entre actividades
        val intent = Intent(this, LoginActivity::class.java)

        // Limpiamos la pila de actividades
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        // LLamar actividad de login
        startActivity(intent)
    }
}