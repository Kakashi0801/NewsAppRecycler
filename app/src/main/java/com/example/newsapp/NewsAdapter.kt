package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val listener:NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {
    private  val items:ArrayList<News> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.card_view,parent,false)
        val holder =NewsViewHolder(view)
        view.setOnClickListener{
            listener.onitemClicked(items[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int {
        return  items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.title.text=currentItem.title
        holder.author.text=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imgUrl).into(holder.imageView)

    }

    fun updateNews(updatedItems:ArrayList<News>){
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }
}

class NewsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val imageView:ImageView=itemView.findViewById(R.id.image)
    val author:TextView = itemView.findViewById(R.id.author)
    val title:TextView= itemView.findViewById(R.id.title)

}