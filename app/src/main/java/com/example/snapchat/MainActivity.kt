package com.example.snapchat

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var pwEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.editText)
        pwEditText = findViewById(R.id.editText2)

        if (mAuth.currentUser != null) {
            logIn()
        }

    }

    fun logIn() {
        var intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

    fun goClicked(view : View){
        Log.i("status","button pressed")
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), pwEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()

                } else {
                    Log.i("status","inside else")
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), pwEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                task.result?.user?.uid?.let {
                                    FirebaseDatabase.getInstance().getReference().child("users").child(
                                        it
                                    ).child("email").setValue(emailEditText?.text.toString())
                                }
                                logIn()
                            }
                            else {
                                makeText(applicationContext,"Login Failed. Try Again..",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

}
