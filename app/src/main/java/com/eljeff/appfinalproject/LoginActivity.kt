package com.eljeff.appfinalproject

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    //inicialir firebase
    private lateinit var auth: FirebaseAuth

    // Variables para comprovar registro
    private var userNameRegister: String? = null
    private var emailRegister: String? = null
    private var passwordRegister: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //Firebase
        auth = Firebase.auth


        // Variables para el login
        var login: Boolean
        var passwNotEmpty: Boolean
        var ussNotEmpty: Boolean
        var passwCorrect: Boolean
        var ussCorrect: Boolean


        // Cuando se llama el boton de loguearse
        loginBinding.loginButton.setOnClickListener {

            //Limpiar los erroes
            cleanErrors()
            signIn()

            /*
            // Recivimos el objeto usuario y extraemos datos
            val userRegister: User = intent.extras?.getSerializable("user") as User
            userNameRegister = userRegister.userName
            emailRegister = userRegister.email
            passwordRegister = userRegister.password

            // Capturamos datos de la interfaz
            val nameEmailLogin = loginBinding.userEmailLogEdTx.text.toString()
            val passwordLogin = loginBinding.passwordLogEdTx.text.toString()

            //# **************************************************************************************

            // Verificar si el campo de email no está vacio
            ussNotEmpty = nameEmailLogin.isNotEmpty()
            passwNotEmpty = passwordLogin.isNotEmpty()



            if(!ussNotEmpty){
                loginBinding.userEmailLogTxInpLay.error = getString(R.string.input_user_email_error)
            }

            // Verificar nombre o correos iguales
            ussCorrect = ((nameEmailLogin == userNameRegister) || (nameEmailLogin == emailRegister))

            // Imprimir error en correo o usuario
            if(!ussCorrect && ussNotEmpty){
                loginBinding.userEmailLogTxInpLay.error = getString(R.string.user_email_error)
                Toast.makeText(this, getString(R.string.user_nonexistent), Toast.LENGTH_SHORT).show()
            }

            // Error si la contraseña esta vacia
            if(!passwNotEmpty && ussCorrect){
                loginBinding.passwordLogTxInpLay.error = getString(R.string.input_pasword_error)
            }



            // Verificar contraseñas iguales

            passwCorrect = (passwordLogin == passwordRegister)


            if(!passwCorrect && ussCorrect && passwNotEmpty){
                loginBinding.passwordLogTxInpLay.error =
                    getString(R.string.password_not_equal)
            }

            login = passwCorrect && ussCorrect



            //# **************************************************************************************

            if (login) {
                // Contenedor para tranferir datos entre actividades
                val intent = Intent(this, MainActivity::class.java)

                // Mandar datos a otra actividad
                intent.putExtra("user", userRegister)

                // Limpiamos la pila de actividades
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                )

                // LLamar actividad de login
                startActivity(intent)
            }
            */
        }

        // Cuando se llama el edit text para Registrarse
        loginBinding.loginRegister.setOnClickListener {

            // Contenedor para tranferir datos entre actividades
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }

        loginBinding.userEmailLogEdTx.setOnClickListener {
            loginBinding.userEmailLogTxInpLay.error = null
        }
        loginBinding.passwordLogEdTx.setOnClickListener {
            loginBinding.passwordLogTxInpLay.error = null
        }

    }

    private fun signIn() {
        // Capturamos datos de la interfaz
        val email = loginBinding.userEmailLogEdTx.text.toString()
        val password = loginBinding.passwordLogEdTx.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser

                    goToMainActivity()

                } else {
                    var msg = ""
                    if(task.exception?.localizedMessage == "The email address is badly formatted."){
                        msg = "El correo está mal escrito."
                    }
                    else if (task.exception?.localizedMessage == "There is no user record corresponding to this identifier. The user may have been deleted."){
                        msg = "No existe un usuario con ese correo."
                    }
                    else if(task.exception?.localizedMessage == "The password is invalid or the user does not have a password."){
                        msg = "Correo o contraseña invalida."
                    }

                    // If sign in fails, display a message to the user.

                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun goToMainActivity() {
        // Contenedor para tranferir datos entre actividades
        val intent = Intent(this, MainActivity::class.java)

        // Limpiamos la pila de actividades
        intent.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )

        // LLamar actividad de login
        startActivity(intent)
    }

    private fun cleanErrors() {
        with(loginBinding) {
            userEmailLogTxInpLay.error = null
            userEmailLogTxInpLay.error = null
        }
    }
}