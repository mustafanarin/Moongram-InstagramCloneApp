package com.mustafanarin.instagramcloneapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.*
import com.mustafanarin.instagramcloneapp.databinding.ActivityUploadBinding
import java.io.IOException
import java.security.Permission
import java.util.UUID

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        fireStore = Firebase.firestore
        storage = Firebase.storage

        registerLauncher()


    }
    fun paylas(view: View){

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference

        val imageReferance = reference.child("Images").child(imageName)

        if (selectedPicture != null){

            imageReferance.putFile(selectedPicture!!).addOnSuccessListener {
                val uploadedPictureReference = storage.reference.child("Images").child(imageName)
                if(auth.currentUser != null){
                    uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        println(downloadUrl)

                        val postMap = hashMapOf<String,Any>()
                        postMap.put("email",auth.currentUser!!.email!!)
                        postMap.put("yorum",binding.yorumText.text.toString())
                        postMap.put("fotoURL",downloadUrl)
                        postMap.put("tarih", Timestamp.now())

                        fireStore.collection("Post").add(postMap).addOnSuccessListener {

                            finish()
                        }.addOnFailureListener {
                            Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }
                }

            }.addOnFailureListener{
                Toast.makeText(this@UploadActivity,"Fotoğraf seçilirken bir hata meydana geldi!",Toast.LENGTH_LONG).show()
            }

        }

    }

    fun selectImage(view: View){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ContextCompat.checkSelfPermission(this@UploadActivity,android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,android.Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Galleriye gitmek için izne ihtiyacımız var!",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }else{
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }

            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }else{
            if(ContextCompat.checkSelfPermission(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Galleriye gitmek için izne ihtiyacımız var!",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
                }else{
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }else{
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }


    }

    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == RESULT_OK){
                val intentForResult = result.data
                if (intentForResult != null){

                    selectedPicture = intentForResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(
                                this@UploadActivity.contentResolver,
                                selectedPicture!!
                            )
                            var selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.fotoEkle.setImageBitmap(selectedBitmap)
                        } else {
                            var selectedBitmap = MediaStore.Images.Media.getBitmap(
                                this@UploadActivity.contentResolver,
                                selectedPicture
                            )
                            binding.fotoEkle.setImageBitmap(selectedBitmap)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                }
            }


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(this@UploadActivity,"İzne ihtiyacımız var!",Toast.LENGTH_LONG).show()
            }
        }
    }

}