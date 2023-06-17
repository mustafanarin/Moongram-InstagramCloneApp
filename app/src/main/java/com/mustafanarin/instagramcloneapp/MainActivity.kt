package com.mustafanarin.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mustafanarin.instagramcloneapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null){
            val intent = Intent(applicationContext,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun girisYap(view : View){
        val email = binding.girisEmail.text.toString()
        val parola = binding.girisParola.text.toString()
        if (email.isNotEmpty() && parola.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,parola).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"Welcome: ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
                    val intent = Intent(this,FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    fun kayıtOl(view: View){

        val intent = Intent(this@MainActivity,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun unuttum(view: View){
        val intent = Intent(this@MainActivity, PasswordActivity::class.java)
        startActivity(intent)

    }
}