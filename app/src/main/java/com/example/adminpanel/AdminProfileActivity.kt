package com.example.adminpanel

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpanel.databinding.ActivityAdminProfileBinding
import com.example.adminpanel.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.saveInfoButton.setOnClickListener {
            upDataUserData()
        }
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInfoButton.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener{
            isEnable = ! isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            if(isEnable){
                binding.name.requestFocus()
            }
            binding.saveInfoButton.isEnabled = isEnable
        }
        retriveUserData()
    }


    private fun retriveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null){
          val userReference = adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var ownerName = snapshot.child("name").getValue()
                        var email = snapshot.child("email").getValue()
                        var password = snapshot.child("password").getValue()
                        var address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()
                        Log.d("TAG","onDataChanged:$ownerName")
                        setDataToTextView(ownerName,email,password,address,phone)

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.password.setText(password.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
    }
    private fun upDataUserData() {
         val updateName = binding.name.text.toString()
        val updateEmail =binding.email.text.toString()
        val updatePassword = binding.password.text.toString()
        val updateAddress = binding.address.text.toString()
        val updatePhone =  binding.phone.text.toString()
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null){
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("address").setValue(updateAddress)
            userReference.child("phone").setValue(updatePhone)
            Toast.makeText(this, "Profile Updated Successful", Toast.LENGTH_SHORT).show()
            //Update the email and password for firebase Authentication

            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }else {
            Toast.makeText(this, "Profile Updated Fail", Toast.LENGTH_SHORT).show()
        }
    }

}

