package com.example.adminpanel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminpanel.databinding.ActivityLoginBinding
import com.example.adminpanel.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
            private lateinit var auth: FirebaseAuth
            private lateinit var database: DatabaseReference
            private lateinit var email:String
            private lateinit var password:String
            private val binding: ActivityLoginBinding by lazy {
                ActivityLoginBinding.inflate(layoutInflater)
            }

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(binding.root)

                //initalize Firebase Auth
                auth = FirebaseAuth.getInstance()

                //initalize Firebase database
                database = Firebase.database.reference

                binding.loginButton.setOnClickListener {
                    //get text from edit text
                    email=binding.email.text.toString().trim()
                    password=binding.password.text.toString().trim()
                    if(email.isBlank()||password.isBlank()){
                        Toast.makeText(this,"Please fill all details", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        createUserAccount(email,password)
                        Toast.makeText(this,"Login Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.dontHaveAccount.setOnClickListener {
                    val intent = Intent(this, SignupActivity::class.java)
                    startActivity(intent)
                }
            }
            private fun createUserAccount(email: String, password: String) {
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user=auth.currentUser
                        updateUi(user)
                    }
                    else {
                        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                            if(task.isSuccessful) {
                                val user=auth.currentUser
                                saveUserData()
                                updateUi(user)
                            }
                            else {
                                Toast.makeText(this,"Authentication failed",Toast.LENGTH_SHORT).show()
                                Log.d("Account","createUserAccount:Authentication failed",task.exception)
                            }

                        }
                    }
                }
            }
    override fun onStart(){
        super.onStart()
        val currentUser=auth.currentUser
        if (currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
            private fun saveUserData() {
                //get text from edit text
                email=binding.email.text.toString().trim()
                password=binding.password.text.toString().trim()
                val user = UserModel(email, password)
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    database.child("user").child(it).setValue(user)
                }
            }
            private fun updateUi(user: FirebaseUser?) {
                    if (user != null) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // important to prevent going back to login
                    }
                }

            }


