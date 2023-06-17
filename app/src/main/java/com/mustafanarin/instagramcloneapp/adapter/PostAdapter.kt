package com.mustafanarin.instagramcloneapp.adapter



import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mustafanarin.instagramcloneapp.databinding.RecyclerRowBinding
import com.mustafanarin.instagramcloneapp.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*



class PostAdapter(private val postArraylist :ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.email.text = postArraylist.get(position).email
        holder.binding.yorum.text = postArraylist.get(position).yorum
        Picasso.get().load(postArraylist[position].fotoURL).into(holder.binding.imageView)
        println(postArraylist[position].fotoURL)

        holder.itemView.begen.setOnClickListener {
            Toast.makeText(it.context, "Beğendiniz!", Toast.LENGTH_LONG).show()
        }

    }



    override fun getItemCount(): Int {
        return postArraylist.size
    }
}










