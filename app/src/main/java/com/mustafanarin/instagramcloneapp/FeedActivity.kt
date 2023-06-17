package com.mustafanarin.instagramcloneapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mustafanarin.instagramcloneapp.adapter.PostAdapter
import com.mustafanarin.instagramcloneapp.databinding.ActivityFeedBinding
import com.mustafanarin.instagramcloneapp.model.Post

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var PostArrayList : ArrayList<Post>
    private lateinit var postAdapter : PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore
        PostArrayList = ArrayList<Post>()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this@FeedActivity)
        postAdapter = PostAdapter(PostArrayList)
        binding.recyclerView.adapter = postAdapter
    }
    private fun getData(){
        db.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            if (value != null){

                PostArrayList.clear()

                val documents = value.documents
                for (document in documents){

                    val yorum = document.get("yorum") as String
                    val email = document.get("email") as String
                    val fotoURL = document.get("fotoURL") as String

                    val post = Post(email,yorum,fotoURL)
                    PostArrayList.add(post)

                }

                postAdapter.notifyDataSetChanged()

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.upload_menu){
            val intent = Intent(this@FeedActivity,UploadActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == R.id.out_menu){
            auth.signOut()
            val intent = Intent(this@FeedActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}