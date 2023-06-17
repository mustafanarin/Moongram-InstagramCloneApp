package com.mustafanarin.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mustafanarin.instagramcloneapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email : String
    private lateinit var parola : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth



    }
    fun kaydet(view :View){
        email = binding.uyeEmail.text.toString()
        parola = binding.uyeParola.text.toString()

        if(email.isNotEmpty() && parola.isNotEmpty()){

            auth.createUserWithEmailAndPassword(email,parola).addOnSuccessListener {
                Toast.makeText(this@RegisterActivity,"Kayıt başarılı!",Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Toast.makeText(this@RegisterActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@RegisterActivity,"Lütfen bilgilerinizi eksizsiz giriniz!",Toast.LENGTH_LONG).show()
        }
    }

    fun girisYap(view: View){

        email = binding.uyeEmail.text.toString()
        parola = binding.uyeParola.text.toString()

        if(email.isNotEmpty() && parola.isNotEmpty()){

            auth.signInWithEmailAndPassword(email,parola).addOnSuccessListener {

                val intent = Intent(this@RegisterActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@RegisterActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@RegisterActivity,"Lütfen bilgilerinizi eksiksiz giriniz!",Toast.LENGTH_LONG).show()
        }
    }
}