package com.eljeff.appfinalproject

import java.io.Serializable

// Clase Usuarios con dos atributos
data class User(
    var userName: String? = null,
    var email: String? = null,
    var password: String? = null
) : Serializable

