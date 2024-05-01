package com.example.sangeet

import android.Manifest
import android.content.pm.PackageManager
import android.media.session.MediaSession
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.Screens.HomeScreen
import com.example.sangeet.Screens.ShowCategory
import com.example.sangeet.Screens.ShowFavouriteSongs
import com.example.sangeet.Screens.SongPlayScreen
import com.example.sangeet.Screens.SongsList
import com.example.sangeet.Screens.SplashScreen
import com.example.sangeet.model.Category
import com.example.sangeet.model.MyExoPlayer
import com.example.sangeet.model.Songs
import com.example.sangeet.ui.theme.SangeetTheme

import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        setContent {
            SangeetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(viewModel)

                }
            }
        }




    }



}




@Composable
fun App(myViewModel: MyViewModel) {
    val navController= rememberNavController()


    BackHandler(enabled = true) {
        myViewModel.songList.value= emptyList()



    }

    NavHost(navController = navController, startDestination = "Splash" ){
        composable(route="Home"){
            HomeScreen(navController,modifier = Modifier,myViewModel)


        }
        composable(route= "Splash"){
            SplashScreen(navController,myViewModel)

        }

        composable(route="ShowSongList"){

            LaunchedEffect(Unit) {

                myViewModel.songList.value= emptyList()
                myViewModel.fetchSongList(myViewModel.getCategory())

            }
            SongsList(myViewModel.getCategory(),myViewModel,navController)
        }

        composable(route="SongPlayScreen"){
            SongPlayScreen(myViewModel=myViewModel)
        }

        composable(route="FavouriteSongsScreen"){
            ShowFavouriteSongs(myViewModel = myViewModel, navController = navController)
        }
    }





}
