package com.example.sangeet.Screens

import android.content.Context
import android.provider.CalendarContract
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sangeet.MyViewModel
import com.example.sangeet.R

import com.example.sangeet.model.MyExoPlayer
import com.example.sangeet.model.MyExoPlayer.isPlaying
import com.example.sangeet.model.Songs
import java.util.concurrent.TimeUnit


@OptIn(UnstableApi::class)
@Composable
fun SongPlayScreen(myViewModel: MyViewModel) {
    val songs= remember {
        mutableStateOf(myViewModel.currentSong)
    }

    myViewModel.isClicked.value=true








    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Now Playing",
            fontSize = 38.sp,


            )

        AsyncImage(
            model = songs.value?.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(10.dp),
            contentScale = ContentScale.Crop



        )

        songs.value?.let {
            Text(text = it.title,
                fontSize = 24.sp,
                fontFamily = FontFamily.Default
            )
        }

        songs.value?.let {
            Text(text = it.subtitle,
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Thin
            )
        }

        val exoPlayer = MyExoPlayer.getInstance() !!
        ShowPlayerView(myExoPlayer = exoPlayer,myViewModel=myViewModel, onSongChanged = {
            songs.value=it!!
        })

        var totalDuration by remember { mutableStateOf(0L) }

        var currentTime by remember { mutableStateOf(0L) }

        var bufferedPercentage by remember { mutableStateOf(0) }

        Box(modifier = Modifier) {
            DisposableEffect(key1 = Unit) {
                val listener =
                    object : Player.Listener {
                        override fun onEvents(player: Player, events: Player.Events) {
                            super.onEvents(player, events)
                            totalDuration = player.duration.coerceAtLeast(0L)
                            currentTime = player.currentPosition.coerceAtLeast(0L)
                            bufferedPercentage = player.bufferedPercentage
                        }
                    }

                exoPlayer.addListener(listener)

                onDispose {
                    exoPlayer.removeListener(listener)
//                    exoPlayer.release()
                }
            }
        }

        BottomControls(
            totalDuration = { totalDuration },
            currentTime = { currentTime },
            bufferPercentage = { bufferedPercentage },
            onSeekChanged = { timeMs: Float -> exoPlayer.seekTo(timeMs.toLong()) }
            )

    }


}












@Composable
fun ShowPlayerView(myExoPlayer: ExoPlayer, myViewModel: MyViewModel,
                   onSongChanged: (Songs?) -> Unit

) {

    val myContext= LocalContext.current


    
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        IconButton(onClick = {
            myViewModel.playPreviousSong()
            MyExoPlayer.startPlaying(myViewModel.songList.value[myViewModel.currentSongIndex.intValue],myContext,myViewModel)
            onSongChanged(myViewModel.songList.value[myViewModel.currentSongIndex.intValue])

        }) {
            Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = null)

            
        }
        
        IconButton(onClick = {

            val currentPosition = myExoPlayer.currentPosition
            val newPosition = currentPosition - 5000 // 5 seconds in milliseconds
            if (newPosition > 0) {
                myExoPlayer.seekTo(newPosition)
            } else {
                myExoPlayer.seekTo(0) // Avoid seeking before the beginning
            }



        }) {
            Icon(imageVector = Icons.Default.Replay5, contentDescription = null)
            
        }
        
        
        IconButton(onClick = {
            isPlaying.value=!isPlaying.value
            if(!isPlaying.value){
                myExoPlayer.pause()


            }
            else{
                myExoPlayer.play()
            }
        }) {
            if(isPlaying.value){
                Icon(imageVector = Icons.Filled.Pause, contentDescription = null)

            }
            else{
                Icon(imageVector = Icons.Filled. PlayArrow, contentDescription = null)


            }



        }
        
        IconButton(onClick = {

            val currentPosition = myExoPlayer.currentPosition
            val newPosition = currentPosition + 10000 // 10 seconds in milliseconds
            val mediaDuration = myExoPlayer.duration
            if (newPosition < mediaDuration) {
                myExoPlayer.seekTo(newPosition)
            } else {
                myExoPlayer.seekTo(mediaDuration) // Avoid seeking beyond the end
            }

        }) {
            Icon(imageVector = Icons.Default.Forward10, contentDescription = null)
            
        }





        IconButton(onClick = {
            myViewModel.playNextSong()
            MyExoPlayer.startPlaying(myViewModel.songList.value[myViewModel.currentSongIndex.intValue],myContext,myViewModel)
            onSongChanged(myViewModel.songList.value[myViewModel.currentSongIndex.intValue])





        }) {
            Icon(imageVector = Icons.Filled.SkipNext, contentDescription = null)

        }


        



        
        
        
        
    }
    
    
}


@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit
) {

    val duration = remember(totalDuration()) { totalDuration() }

    val videoTime = remember(currentTime()) { currentTime() }

    val buffer = remember(bufferPercentage()) { bufferPercentage() }

    Column(modifier = modifier.padding(bottom = 32.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // buffer bar
            Slider(
                value = buffer.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/},
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )

            // seek bar
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = videoTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..duration.toFloat(),
                colors =
                SliderDefaults.colors(
                    thumbColor = Color.Cyan,
                    activeTickColor =Color.Gray
                )
            )
        }

        
    }
}






