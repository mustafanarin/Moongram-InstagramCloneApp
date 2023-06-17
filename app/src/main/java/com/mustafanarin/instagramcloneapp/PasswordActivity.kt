package com.mustafanarin.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mustafanarin.instagramcloneapp.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

    }

    fun sifirla(view: View){
        val email = binding.pSifirlaEmail.text.toString().trim()
        if (TextUtils.isEmpty(email)){
            binding.pSifirlaEmail.error = "Lütfen e-mail adresinizi yazınız!"
        }else{
            auth.sendPasswordResetEmail(email).addOnCompleteListener { sifirlama ->
                if (sifirlama.isSuccessful){
                    binding.pSifirlaMesaj.text = "E-mail adresinize sıfırlama bağlantısı gönderildi.Lütfen kontrol ediniz."
                }else{
                    binding.pSifirlaMesaj.text = "Sıfırlama işlemi başarısız."
                }
            }
        }
    }

    fun girisYap(view: View){
        val intent = Intent(this@PasswordActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}