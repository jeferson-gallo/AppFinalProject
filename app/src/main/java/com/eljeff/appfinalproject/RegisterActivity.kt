package com.eljeff.appfinalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eljeff.appfinalproject.databinding.ActivityRegisterBinding
import com.eljeff.appfinalproject.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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
                        createUser(email)


                    } else {
                        var msg = ""
                        // If sign in fails, display a message to the user.

                        if(task.exception?.localizedMessage == "The email address is badly formatted."){
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

    private fun createUser(email: String) {
        // Preguntamos por el id del usurario
        val uid = auth.currentUser?.uid

        // creamos el usuario
        uid?.let { uid ->
            val user = Users(
                uid = uid,
                name = "Jeferson",
                email = email,
                address = "123",
                telephone = "456",
                phone = "789",
                score = 50
            )

            // Instanciamos la base de datos
            val db = Firebase.firestore

            // Add a new document with a generated ID
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("createInDB", "DocumentSnapshot added with ID: ${documentReference.id}")
                    goToLoginActivity()
                }
                .addOnFailureListener { e ->
                    Log.w("createInDB", "Error adding document", e)
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
