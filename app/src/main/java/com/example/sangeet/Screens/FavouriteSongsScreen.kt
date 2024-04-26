package com.example.sangeet.Screens

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sangeet.MyViewModel

@Composable
fun ShowFavouriteSongs(myViewModel: MyViewModel,navController:NavController) {
    val songList= remember {
        myViewModel.favouriteSongList

    }
    




    Box(modifier = Modifier.fillMaxSize()){

        Column(modifier = Modifier
            .fillMaxSize()

            .padding(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Favourites", fontSize = 42.sp, modifier = Modifier.padding(5.dp))
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp)
                )

            }
            Spacer(modifier = Modifier.height(10.dp))


                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier
                        .fillMaxSize()
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

        if (myViewModel.isClicked.value) {

            SongDetail(myViewModel = myViewModel, modifier = Modifier.align(Alignment.BottomCenter), navController)

        }






    }






    }







