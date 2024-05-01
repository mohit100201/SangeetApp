package com.example.sangeet.Screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sangeet.MyViewModel
import com.example.sangeet.model.Category
import com.example.sangeet.model.MyExoPlayer
import com.example.sangeet.model.Songs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

@Composable
fun SongsList(category: Category,myViewModel: MyViewModel,navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)

            , horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = category.coverUrl, // Replace with your Firebase Storage image URL
                contentDescription = "Your image description",
                modifier = Modifier
                    .size(200.dp)

                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop

            )




            Text(
                text = category.name.toString(),
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(top = 10.dp),
                textAlign = TextAlign.Center

            )


            val songList = remember {
                myViewModel.songList

            }

            if (myViewModel.isListEmpty()) {
                // Show a loading indicator while data is being fetched
                val color = if (isSystemInDarkTheme()) {
                    Color.White

                } else {
                    Color.Black

                }
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(32.dp),
                    color = color


                ) // Or custom loading UI
            } else {

                val context = LocalContext.current


                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(5f)
                        .padding(top = 5.dp)

                ) {
                    itemsIndexed(songList.value) { index, item ->
                        ShowSong(
                            songs = item,
                            modifier = Modifier,
                            myViewModel,
                            navController,
                            index
                        )


                    }
                }


            }
            
           if(myViewModel.isClicked.value){
               Spacer(modifier = Modifier.height(80.dp))
           }



        }

        val songs = myViewModel.currentSong

        if (myViewModel.isClicked.value) {

            SongDetail(myViewModel = myViewModel, modifier = Modifier.align(Alignment.BottomCenter), navController)

        }




    }





    }




@Composable
fun SongDetail(myViewModel: MyViewModel,modifier: Modifier,navController: NavController) {
    val songs= myViewModel.currentSong ?: return
    myViewModel.isFavourite()








    Row (
        modifier = modifier.padding(10.dp)
    ){
        Card (
            modifier = modifier
                .fillMaxWidth()


                .clickable {
                    navController.navigate("SongPlayScreen")
                }
            ,
            elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),

            ){
            Row (
                modifier= Modifier
                    .padding(5.dp)



                ,
                verticalAlignment = Alignment.CenterVertically
            ){
                AsyncImage(model = songs.coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(7.dp)
                )

                Column (

                ){
                    Text(text = "Now Playing", fontFamily = FontFamily.Default, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = songs.title, fontFamily = FontFamily.Default, fontSize = 18.sp, fontWeight = FontWeight.Thin)

                }

                val myExoPlayer=MyExoPlayer.getInstance()


                Row (
                    modifier = Modifier
                        .weight(1f)
                       ,
                    horizontalArrangement = Arrangement.End

                ){
                    IconButton(onClick = {
                        MyExoPlayer.isPlaying.value=!MyExoPlayer.isPlaying.value
                        if(!MyExoPlayer.isPlaying.value){
                            myExoPlayer?.pause()


                        }
                        else{
                            myExoPlayer?.play()
                        }
                    }) {
                        if(MyExoPlayer.isPlaying.value){
                            Icon(
                                imageVector = Icons.Filled.Pause,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )

                        }
                        else{
                            Icon(imageVector = Icons.Filled. PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)

                            )


                        }



                    }
                    val context= LocalContext.current
                    IconButton(onClick = {

                        if(myViewModel.isFav.value){
                           Toast.makeText(context,"Song is Removed from Favourite!",Toast.LENGTH_LONG).show()


                        }
                        else{
                            Toast.makeText(context,"Song is Added to Favourite!",Toast.LENGTH_LONG).show()

                        }

                       myViewModel.isFav.value=!myViewModel.isFav.value



                        myViewModel.updateFavouriteField()





                    }) {



                        if(myViewModel.isFav.value){
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription =null )
                        }
                        else{
                            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null )

                        }

                    }
                }







            }



        }

    }










}




@Composable
fun ShowSong(songs: Songs,modifier: Modifier=Modifier,myViewModel: MyViewModel,navController: NavController,Index:Int) {



    val MYCONTEXT= LocalContext.current



        Card (
            modifier = modifier
                .fillMaxWidth()
                .clickable {

                    MyExoPlayer.startPlaying(songs, MYCONTEXT, myViewModel)
                    myViewModel.currentSongIndex.intValue = Index
                    myViewModel.isClicked.value = true
                    navController.navigate("SongPlayScreen")


                }
            ,
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ){
            Row (
                modifier= Modifier
                    .padding(5.dp)


                ,
                verticalAlignment = Alignment.CenterVertically
            ){
                AsyncImage(model = songs.coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(7.dp)
                )

                Column (

                ){
                    Text(text = songs.title, fontFamily = FontFamily.Default, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = songs.subtitle, fontFamily = FontFamily.Default, fontSize = 15.sp, fontWeight = FontWeight.Thin)
                }


            }

        }







}


