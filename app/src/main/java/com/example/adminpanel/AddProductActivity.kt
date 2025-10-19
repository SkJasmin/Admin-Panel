package com.example.adminpanel

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpanel.databinding.ActivityAddProductBinding
import com.example.adminpanel.model.AllProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AddProductActivity : AppCompatActivity() {
    //product item details
    private lateinit var productName:String
    private lateinit var productPrice:String
    private lateinit var productDis:String
    private  var productImageUri: Uri? = null
    private lateinit var discount:String
    private lateinit var productsLeft:String
    private lateinit var deliveryCharges:String
    private lateinit var productHighlights:String
    //Firebase
    private lateinit var auth: FirebaseAuth
    private  lateinit var database: FirebaseDatabase

    private val binding : ActivityAddProductBinding by lazy {
        ActivityAddProductBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    //initalize Firebase
        auth = FirebaseAuth.getInstance()
        //initalize Firebase database instance
        database = FirebaseDatabase.getInstance()

        binding.AddProductsButton.setOnClickListener {
            //Get data from Field
            productName=binding.productName.text.toString().trim()
            productPrice=binding.productPrice.text.toString().trim()
            productDis=binding.shortDiscription.text.toString().trim()
            discount=binding.discount.text.toString().trim()
            productsLeft=binding.productsLeft.text.toString().trim()
            deliveryCharges=binding.delivery.text.toString().trim()
            productHighlights=binding.productHighlights.text.toString().trim()

            if(!(productName.isBlank() || productPrice.isBlank()|| productsLeft.isBlank()||discount.isBlank()||deliveryCharges.isBlank()||productDis.isBlank()||productHighlights.isBlank()))
            {
                uploadData()
                Toast.makeText(this,"Product added Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this,"Fill all the details", Toast.LENGTH_SHORT).show()
            }

        }
         binding.selectImage.setOnClickListener {
             pickImage.launch("image/*")
         }


        binding.backButton.setOnClickListener {
            finish()
        }
    }
     //image is not Uploaded in the database but the url is saved
    private fun uploadData() {
    val menuRef = database.getReference("menu")
    val newItemKey = menuRef.push().key

    if (newItemKey != null) {
        // if image is selected but we cannot upload (Free plan)
        val imagePath = productImageUri?.toString() ?: ""  // just save URI string or empty

        val newItem = AllProduct(
            newItemKey,
            productName = productName,
            productPrice = productPrice,
            productDis = productDis,
            discount = discount,
            deliveryCharges = deliveryCharges,
            productHighlights = productHighlights,
            productsLeft = productsLeft,
            productImage = imagePath   // no Firebase Storage, just keep it blank or local URI
        )

        menuRef.child(newItemKey).setValue(newItem)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data upload failed", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(this, "Key generation failed", Toast.LENGTH_SHORT).show()
    }
}
     /*
    private fun AddProductActivity.uploadData() {
        //get a reference to the "menu" node in the database
        val menuRef= database.getReference("menu")
        //Generate a unique key for the new menu item
        val newItemKey=menuRef.push().key
        if(productImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef=storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(productImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    // Create a new menu item
                    val newItem = AllProduct(
                        productName = productName,
                        productPrice=productPrice,
                        productDis=productDis,
                        discount=discount,
                        deliveryCharges = deliveryCharges,
                        productHighlights = productHighlights,
                        productsLeft = productsLeft,
                        productImage = downloadUrl.toString()
                    )
                    newItemKey?.let{
                        key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"data uploaded Successfully",Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this,"data uploaded Failed",Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            }.addOnFailureListener {
                    Toast.makeText(this,"Image uploaded Failed",Toast.LENGTH_SHORT).show()
                }
        }else {
            Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show()
        }

    }*/
   private val pickImage=registerForActivityResult(ActivityResultContracts.GetContent())
   { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            productImageUri=uri
        }

    }

}


