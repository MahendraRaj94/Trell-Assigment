package com.mahendra.trellsample.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.mahendra.trellsample.BuildConfig
import com.mahendra.trellsample.model.VideoItem
import java.text.SimpleDateFormat
import java.util.*

class PrefUtils {

    companion object{
        private var sharedPreferences : SharedPreferences? = null;
        fun getSharedPreferences(context: Context) : SharedPreferences?{
            if(sharedPreferences == null){
                sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
            }
            return sharedPreferences;
        }
        fun saveFavorite(context: Context,videoItem: VideoItem,fav : Boolean){
            getSharedPreferences(context)?.edit()?.putBoolean(videoItem.id.toString(),fav)
                ?.apply()
        }

        fun getFavorite(context: Context,id : Long) : Boolean{
            return  (getSharedPreferences(context) != null && getSharedPreferences(context)!!.getBoolean(id.toString(),false))
        }
    }
}