package com.mahendra.trellsample.view.videolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahendra.trellsample.R
import com.mahendra.trellsample.model.VideoItem
import com.mahendra.trellsample.util.PrefUtils
import java.io.File


class VideoListAdapter(var items : List<VideoItem>,val playButtonListener: PlayButtonListener?) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail : AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.ivThumbnail);
        val ivPlay : AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.ivPlay);
        val ivStar : AppCompatImageView = itemView.findViewById<AppCompatImageView>(R.id.ivStar);
    }

    fun updateVideos(list : List<VideoItem>){
        this.items = list;
        notifyDataSetChanged();
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_video,parent,false))
    }

    override fun getItemCount(): Int {
       return items.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView)
            .load(item.contentUri)
            .into(holder.ivThumbnail)

        if(item.fav)
            holder.ivStar.setImageResource(R.drawable.ic_star_filled)
        else
            holder.ivStar.setImageResource(R.drawable.ic_star_border)

        holder.ivPlay.setOnClickListener {
            playButtonListener?.playButtonClicked(item,holder)
        }

        holder.ivStar.setOnClickListener {
           playButtonListener?.favButtonClicked(item)
        }

    }

    interface PlayButtonListener{
        fun playButtonClicked(videoItem : VideoItem,holder: ViewHolder)
        fun favButtonClicked(videoItem: VideoItem)
    }
}