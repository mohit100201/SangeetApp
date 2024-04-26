package com.example.sangeet.Screens

import android.text.Layout.Alignment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.example.sangeet.MyViewModel
import com.example.sangeet.R
import com.example.sangeet.model.Category
import com.example.sangeet.model.DataManager
import com.example.sangeet.model.MyExoPlayer
import com.example.sangeet.model.Songs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController,modifier: Modifier=Modifier,myViewModel: MyViewModel) {

    Box(modifier=Modifier.fillMaxSize()){
        Column (modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ){
            Row(modifier= Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                StatusBar(myViewModel,navController)
            }

            // horizontal recycler view for category
            Text(
                text = "Categories",

                fontSize = 24.sp,
                modifier=Modifier.padding(top = 10.dp, bottom = 10.dp)

            )

            val categories = myViewModel.categories

            LazyRow(
                modifier=Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)){
                this.items(categories){item: Category ->
                    ShowCategory(category = item,myViewModel=myViewModel, navController = navController)

                }


            }

            // sections
            val punjabiSongs=myViewModel.Punjabi
            Text(
                text = "Punjabi",

                fontSize = 24.sp,
                modifier=Modifier.padding(top=5.dp)


            )

            LazyRow(
                modifier=Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)){
               itemsIndexed(punjabiSongs.value){index, item ->
                   ShowSectionSong(songs = item, navController =navController , myViewModel = myViewModel, Index =index )

               }
               


            }


            Text(
                text = "90's Bollywood Songs",

                fontSize = 24.sp,
                modifier=Modifier.padding(top=5.dp)


            )

            val old=myViewModel.oldSongs

            LazyRow(
                modifier=Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)){
                itemsIndexed(old.value){index, item ->
                    ShowSectionSong(songs = item, navController =navController , myViewModel = myViewModel, Index =index )

                }



            }
            
        if(myViewModel.isClicked.value){
            Spacer(modifier = Modifier.height(100.dp))
        }

           
            

        }



        if(myViewModel.isClicked.value){
            SongDetail(myViewModel = myViewModel, modifier =Modifier.align(androidx.compose.ui.Alignment.BottomCenter),navController )
        }


    }
    

}

@Composable
fun ShowCategory(category: Category,modifier: Modifier=Modifier,myViewModel: MyViewModel,navController: NavController) {




    Column (
        modifier=modifier,


    ){


            AsyncImage(
                model = category.coverUrl, // Replace with your Firebase Storage image URL
                contentDescription = "Your image description",
                modifier = Modifier
                    .size(170.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        myViewModel.setCategory(category)
                        navController.navigate("ShowSongList")


                    }

                    




                ,
                contentScale = ContentScale.Crop

                )
        






        Text(
            text = category.name.toString(),
            fontSize = 18.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier.width(150.dp),
            textAlign = TextAlign.Center

        )


    }
    


}



@Composable
fun StatusBar(myViewModel: MyViewModel,navController: NavController) {
    val scope = rememberCoroutineScope()

    Image(
        painter = painterResource(id = R.drawable.music),
        contentDescription = "logo",
        modifier = Modifier.size(30.dp)
    
    )
    Text( 
        text = "Sangeet",
        fontSize = 28.sp,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)


        
    )
    


    IconButton(onClick = {

        scope.launch(Dispatchers.Main) {
            // Fetch favourite songs
            myViewModel.fetchFavouriteSongs()

            // Navigate to "FavouriteSongsScreen" after songs are fetched
            navController.navigate("FavouriteSongsScreen")
        }






    }) {
        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(30.dp))

    }
    
}

@Composable
fun ShowSectionSong(songs: Songs,navController: NavController,myViewModel: MyViewModel,Index:Int) {
    val context= LocalContext.current.applicationContext



    Column (
        modifier = Modifier.clickable {
            MyExoPlayer.startPlaying(songs,context, myViewModel)
            myViewModel.currentSongIndex.intValue = Index
            myViewModel.isClicked.value = true
            navController.navigate("SongPlayScreen")




        }
    ){
        AsyncImage(model = songs.coverUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(7.dp)


        )

        Text(
            text = songs.title,
            fontSize = 18.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier.width(170.dp),
            textAlign = TextAlign.Center

        )




    }

}



