package com.mahendra.trellsample.view.videolist

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahendra.trellsample.model.VideoItem
import com.mahendra.trellsample.util.PrefUtils
import java.util.concurrent.TimeUnit


class VideoListViewModel : ViewModel() {

    val videoList = MutableLiveData<List<VideoItem>?>()

    fun getVideosFromStorage(context: Context) : MutableLiveData<List<VideoItem>?>{
        val list = ArrayList<VideoItem>();
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
        )

        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        val query = ContentResolverCompat.query(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder,
            CancellationSignal())
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                list.add(VideoItem(id,contentUri, name, duration, size,PrefUtils.getFavorite(context,id)));
            }

            videoList.postValue(list);
        }

        return videoList;
    }
}
