package com.example.jetbookreader.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewmodel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    fun signInWithEmailAndPassword(email: String, password: String,home:()->Unit)=
        viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FB", "signInWithEmailAndPassword: Logged In ${task.result}")
                      home()
                    //  TODO("Take User to Home")
                    } else {
                        Log.d("FB", "signInWithEmailAndPassword: ${task.result}")
                    }

                }

        } catch (ex: Exception) {
            Log.d("FB", "signInWithEmailAndPassword:${ex.message}")
        }

    }

    fun createUserWithEmailAndPassword() {

    }
}