package com.mahendra.trellsample.view.player

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mahendra.trellsample.BuildConfig
import com.mahendra.trellsample.R
import com.mahendra.trellsample.databinding.ExoPlayerFragmentBinding
import com.mahendra.trellsample.model.VideoItem
import com.mahendra.trellsample.util.PrefUtils


class ExoPlayerFragment : Fragment(){

    companion object {
        const val VIDEO_ITEM : String = "videoItem";
        fun newInstance(videoItem: VideoItem) = ExoPlayerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(VIDEO_ITEM,videoItem)
            }
        }
    }

    private lateinit var databinding: ExoPlayerFragmentBinding
    private lateinit var viewModel: ExoPlayerViewModel
    private var player : ExoPlayer? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databinding = DataBindingUtil.inflate<ExoPlayerFragmentBinding>(inflater,R.layout.exo_player_fragment, container, false);
        return databinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val videoItem : VideoItem? = arguments?.getParcelable<VideoItem>(VIDEO_ITEM);
        viewModel = ViewModelProvider(this).get(ExoPlayerViewModel::class.java)
       setToolbar(videoItem);
        context?.let {
           val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                it,
                Util.getUserAgent(it, BuildConfig.APPLICATION_ID)
            )
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoItem?.contentUri)
            player = SimpleExoPlayer.Builder(it).build();
            databinding.playerView.player = player;
            player?.prepare(videoSource)
        }
    }

    private fun setToolbar(videoItem: VideoItem?) {
        databinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        databinding.toolbar.setNavigationOnClickListener{
            activity?.onBackPressed();
        }
        databinding.toolbar.inflateMenu(R.menu.star_menu)

        val menuItem = databinding.toolbar.menu.getItem(0)
        if(videoItem!= null){
            menuItem.isCheckable = videoItem.fav
            if(menuItem!!.isCheckable)
                menuItem.setIcon(R.drawable.ic_star_filled)
            else
                menuItem.setIcon(R.drawable.ic_star_border)

        }
        databinding.toolbar.setOnMenuItemClickListener { item ->
            if(item?.itemId == R.id.fav_menu){
                item.isCheckable = !item.isCheckable
                PrefUtils.saveFavorite(context!!,videoItem!!,item.isCheckable)
                if(item!!.isCheckable)
                    item.setIcon(R.drawable.ic_star_filled)
                else
                    item.setIcon(R.drawable.ic_star_border)
            }
            true;
        }
        databinding.toolbar.title = "Trell Videos"

    }

    override fun onDestroyView() {
        player?.release()
        super.onDestroyView()
    }

}
