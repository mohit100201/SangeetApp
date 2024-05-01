package com.example.sangeet.Screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.App
import com.example.sangeet.MainActivity
import com.example.sangeet.R
import com.example.sangeet.ui.theme.SangeetTheme
import com.example.sangeet.ui.theme.md_theme_light_primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangeet.MyViewModel
import com.example.sangeet.model.Category
import com.example.sangeet.model.DataManager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SplashScreen(navController: NavController,myViewModel: MyViewModel) {



    LaunchedEffect(key1 = Unit ){


        delay(2000)
        navController.navigate("Home")

    }

    if(myViewModel.isStarted){
        myViewModel.fetchCategories()
        myViewModel.fetchSection()
        myViewModel.fetchSectionOLD()
        myViewModel.isStarted=false
    }










    Column (modifier = Modifier
        .fillMaxSize()

        .background(md_theme_light_primary)
        , verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(painter = painterResource(id = R.drawable.music), contentDescription = "music player logo")
        Text(text = "Sangeet", style = MaterialTheme.typography.bodyLarge)

    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally

    ){

        CircularProgressIndicator(Modifier.size(32.dp))



    }



}