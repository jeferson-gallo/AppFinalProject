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



        // Al presionar el boton de regsitrar
        registerBinding.registerButton.setOnClickListener {

            //Limpiar los erroes
            cleanErrors()

            val userNameFrom = registerBinding.userNameRegEdTx.text.toString()
            val emailFrom = registerBinding.emailRegEdTx.text.toString()
            val passwordFrom = registerBinding.passwordRegEdTx.text.toString()
            val confPasswordFrom = registerBinding.confPasswordEdTx.text.toString()

            //**************************************************************************************

            // Verificamos campos vacios
            val nameNotempty = userNameFrom.isNotEmpty()
            val emailNotempty = emailFrom.isNotEmpty()
            val passwNotempty = passwordFrom.isNotEmpty()
            val confPasswNotempty = confPasswordFrom.isNotEmpty()
            val acceptPassw = passwordFrom.length >= 6

            //Si no se ingresa usuario
            if(!nameNotempty){
                registerBinding.userNameRegTxInpLay.error = getString(R.string.input_user_name_error)
            }

            //Si no se ingresa email
            if(!emailNotempty){
                registerBinding.emailRegTxInpLay.error = getString(R.string.input_email_error)
            }

            //Si no se ingresa contraseña
            if(!passwNotempty){
                registerBinding.passwordRegTxInpLay.error = getString(R.string.input_pasword_error)
            }

            //Si no se ingresan mas de 6 caracteres
            if(!acceptPassw && passwNotempty){
                registerBinding.passwordRegTxInpLay.error = getString(R.string.digit_min)
            }

            // Si se ingresan los 6 caracteres pero el campo de repetir está vacio
            if(acceptPassw && !confPasswNotempty){
                registerBinding.confPasswordRegTxInpLay.error = getString(R.string.repeat_password)
            }

            // Si se ingresan mas de 6, si no está vacio y si las contraseñas son iguales
            if( (passwordFrom != confPasswordFrom) && acceptPassw && confPasswNotempty){
                registerBinding.confPasswordRegTxInpLay.error = getString(R.string.password_not_equal)
            }


            val sent = nameNotempty && emailNotempty && acceptPassw && (passwordFrom == confPasswordFrom)

            //**************************************************************************************

            if (sent) {
                // asignamos datos del formulario
                val user = User(
                    userName = userNameFrom,
                    email = emailFrom,
                    password = passwordFrom
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

        registerBinding.userNameRegEdTx.setOnClickListener {
            registerBinding.userNameRegTxInpLay.error = null
        }
        registerBinding.emailRegEdTx.setOnClickListener {
            registerBinding.emailRegTxInpLay.error = null
        }
        registerBinding.passwordRegEdTx.setOnClickListener {
            registerBinding.passwordRegTxInpLay.error = null
        }
        registerBinding.confPasswordEdTx.setOnClickListener {
            registerBinding.confPasswordRegTxInpLay.error = null
        }

    }

    private fun cleanErrors() {
        with(registerBinding){
            userNameRegTxInpLay.error = null
            emailRegTxInpLay.error = null
            passwordRegTxInpLay.error = null
            confPasswordRegTxInpLay.error = null
        }
    }
}
