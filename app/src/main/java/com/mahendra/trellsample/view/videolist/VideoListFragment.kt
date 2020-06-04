package com.mahendra.trellsample.view.videolist

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mahendra.trellsample.R
import com.mahendra.trellsample.databinding.VideoListFragmentBinding
import com.mahendra.trellsample.model.VideoItem
import com.mahendra.trellsample.util.PrefUtils
import com.mahendra.trellsample.view.MainActivity
import com.mahendra.trellsample.view.player.ExoPlayerFragment

class VideoListFragment : Fragment(), VideoListAdapter.PlayButtonListener {

    companion object {
        fun newInstance() = VideoListFragment()
    }

    private lateinit var adapter: VideoListAdapter
    private lateinit var viewModel: VideoListViewModel
    private lateinit var dataBinding : VideoListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(layoutInflater,R.layout.video_list_fragment,container,false);
        return dataBinding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VideoListViewModel::class.java)
        dataBinding.toolbar.title = "Trell Videos"
        dataBinding.rvVideos.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        adapter = VideoListAdapter(ArrayList(),this);
        dataBinding.rvVideos.adapter = adapter;
        checkStoragePermission()

    }

    private fun checkStoragePermission() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100);
            } else {
              setViewModelObserver()
            }
        }
    }

    private fun setViewModelObserver() {
        context?.let {
            viewModel?.getVideosFromStorage(it).observe(viewLifecycleOwner,
                Observer<List<VideoItem>?> {
                    it?.let {
                        adapter.updateVideos(it)
                    }
                })
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 100 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setViewModelObserver()
        }else{
            Toast.makeText(activity,"Please provide required permission",Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun playButtonClicked(videoItem: VideoItem,holder: VideoListAdapter.ViewHolder) {
        val fragment = ExoPlayerFragment.newInstance(videoItem);
       parentFragmentManager.beginTransaction().replace(R.id.rootView,fragment,ExoPlayerFragment.javaClass.name)
            .addToBackStack(VideoListFragment::javaClass.name)
            .addSharedElement(holder.ivThumbnail,"ivThumbnail")
            .commit()
    }

    override fun favButtonClicked(item: VideoItem) {
//        context?.let {
//            PrefUtils.saveFavorite(it, item, !item.fav)
//            adapter.notifyDataSetChanged()
//            if (item.fav) {
//                Toast.makeText(context, "Added to Favorites!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context, "Removed from Favorites!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

}
