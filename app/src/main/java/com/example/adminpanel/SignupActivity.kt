package com.example.adminpanel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminpanel.databinding.ActivitySignupBinding
import com.example.adminpanel.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameofShop:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // initalizing firebase uth
        auth = FirebaseAuth.getInstance()
        //initalize Firebase database
        database = Firebase.database.reference
        binding.signUpButton.setOnClickListener {
            //get text from edittext
            userName= binding.name.text.toString().trim()
            nameofShop = binding.nameOfShop.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(userName.isBlank() || nameofShop.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else{
                createAccount(email,password)
            }
        }
        binding.alreadyHaveAnAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val localeList = arrayOf("Banglore","Gujarat","Odisa","Jaipur","Sikar","Hyderabad","Delhi")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,localeList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(this,"Account created Successfully",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Failed",task.exception)
            }
        }
    }
    //save data into database
    private fun saveUserData() {
        //get text from edittext
        userName= binding.name.text.toString().trim()
        nameofShop = binding.nameOfShop.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user= UserModel(userName, nameofShop, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }
}