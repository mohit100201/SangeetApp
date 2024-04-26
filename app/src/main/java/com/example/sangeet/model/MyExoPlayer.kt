package com.example.sangeet.model


import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.sangeet.MyViewModel


object MyExoPlayer {

    private var exoPlayer: ExoPlayer? = null

    var isPlaying= mutableStateOf(true)
    var currSongs:Songs?=null
   
    

    fun getInstance(): ExoPlayer? {
        return exoPlayer
    }


    fun startPlaying(song: Songs,context: Context,myViewModel: MyViewModel) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()


        }








          if(myViewModel.currentSong!=song){
              isPlaying.value=true
              myViewModel.currentSong = song
              myViewModel.currentSong?.songUrl?.apply {
                  val mediaItem = MediaItem.fromUri(this)
                  exoPlayer?.setMediaItem(mediaItem)
                  exoPlayer?.prepare()
                  exoPlayer?.playWhenReady = true




              }
          }










    }





}



