package com.eljeff.appfinalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding


    //inicialir firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        //Firebase
        auth = Firebase.auth

        // Al presionar el boton de regsitrar
        registerBinding.registerButton.setOnClickListener {

            //Limpiar los erroes
            cleanErrors()
            registerUser()

            /*
            val userNameFrom = registerBinding.userNameRegEdTx.text.toString()
            val emailFrom = registerBinding.emailRegEdTx.text.toString()
            val passwordFrom = registerBinding.passwordRegEdTx.text.toString()
            val confPasswordFrom = registerBinding.confPasswordEdTx.text.toString()

            //# **************************************************************************************

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

            //# **************************************************************************************

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

             */
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

    private fun registerUser() {
        val userName = registerBinding.userNameRegEdTx.text.toString()
        val email = registerBinding.emailRegEdTx.text.toString()
        val password = registerBinding.passwordRegEdTx.text.toString()
        val repPassword = registerBinding.confPasswordEdTx.text.toString()

        if(password != repPassword){
            Toast.makeText(this, "Las contraseñas deben ser iguales",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Register", "createUserWithEmail:success")
                        val user = auth.currentUser
                        goToLoginActivity()

                    } else {
                        var msg = ""
                        // If sign in fails, display a message to the user.
                        if(task.exception?.localizedMessage == "createUserWithEmail:success"){
                            msg = "Usuario registrado exitosamente."
                        }
                        else if(task.exception?.localizedMessage == "The email address is badly formatted."){
                            msg = "El correo está mal escrito."
                        }
                        else if(task.exception?.localizedMessage == "The given password is invalid. [ Password should be at least 6 characters ]"){
                            msg = "La contraseña debe tener mínimo 6 caracteres"
                        }
                        else if(task.exception?.localizedMessage == "The email address is badly formatted."){
                            msg = "El correo está mal escrito."
                        }
                        else if(task.exception?.localizedMessage == "The email address is already in use by another account."){
                            msg = "Ya existe un usruario con éste correo electrónico"
                        }


                        Log.w("Register", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, msg,
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }

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

    private fun cleanErrors() {
        with(registerBinding){
            userNameRegTxInpLay.error = null
            emailRegTxInpLay.error = null
            passwordRegTxInpLay.error = null
            confPasswordRegTxInpLay.error = null
        }
    }
}
