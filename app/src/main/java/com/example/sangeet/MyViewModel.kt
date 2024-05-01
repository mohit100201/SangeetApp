package com.example.sangeet

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.example.sangeet.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource

import com.example.sangeet.model.DataManager
import com.example.sangeet.model.MyExoPlayer
import com.example.sangeet.model.MyInterface
import com.example.sangeet.model.Songs
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class MyViewModel():ViewModel(){

    var categories= mutableListOf<Category>()


    var Punjabi=MutableStateFlow<List<Songs>>(emptyList())

    var oldSongs=MutableStateFlow<List<Songs>>(emptyList())

    var songList= MutableStateFlow<List<Songs>>(emptyList())

    var currentCategory=Category()

//    lateinit var myContext:Context

    var isStarted:Boolean=true

    var currentSongIndex= mutableIntStateOf(-1)

    var currentSong:Songs?= null

    var isClicked= mutableStateOf(false)
    var isFav= mutableStateOf(false)




    fun playNextSong(){
        currentSongIndex.intValue=(currentSongIndex.intValue+1)%(songList.value.size)

    }

    fun playPreviousSong(){
        (currentSongIndex.intValue)=(currentSongIndex.intValue-1+songList.value.size)%(songList.value.size)

    }

    fun isListEmpty():Boolean{
        return songList.value.isEmpty()

    }


    fun setCategory(category: Category){
        this.currentCategory =category
    }

    fun getCategory():Category{
        return this.currentCategory

    }

    fun fetchCategories()= CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot=Firebase.firestore.collection("category").get().await()



        for(document in querySnapshot.documents){
            val obj=document.toObject<Category>()
            if (obj != null) {


                categories.add(obj)
            }
        }

    }

    fun fetchSongList(category: Category)= CoroutineScope(Dispatchers.IO).launch {
        val collectionRef=Firebase.firestore.collection("songs")
        val querySnapshot=collectionRef.whereIn("id",category.songs).get().await()
        val newSongList = mutableListOf<Songs>()



        for(document in querySnapshot.documents){
            val obj=document.toObject<Songs>()
            if(obj!=null){
                newSongList.add(obj)
            }
        }

        songList.value=newSongList


    }

    fun fetchSection()= CoroutineScope(Dispatchers.IO).launch {
        val QuerySnapshot=Firebase.firestore.collection("sections").get().await()
        var section:Category= Category()

        for( cat in QuerySnapshot.documents ){

            if(cat.toObject<Category>()?.name=="Punjabi"){
                section=cat.toObject<Category>()!!

            }





        }




        val collectionRef=Firebase.firestore.collection("songs")
        val querySnapshot=collectionRef.whereIn("id",section.songs).get().await()
        val newSongList = mutableListOf<Songs>()



        for(document in querySnapshot.documents){
            val obj=document.toObject<Songs>()
            if(obj!=null){
                newSongList.add(obj)
            }
        }

        Punjabi.value=newSongList



    }


    fun fetchSectionOLD()= CoroutineScope(Dispatchers.IO).launch {
        val QuerySnapshot=Firebase.firestore.collection("sections").get().await()
        var section:Category= Category()

        for( cat in QuerySnapshot.documents ){
            if(cat.toObject<Category>()?.name=="90's bollywood songs"){
                section=cat.toObject<Category>()!!


            }




        }

        Log.d("MOHIT",section.songs.toString())




        val collectionRef=Firebase.firestore.collection("songs")
        val querySnapshot=collectionRef.whereIn("id",section.songs).get().await()
        val newSongList = mutableListOf<Songs>()



        for(document in querySnapshot.documents){
            val obj=document.toObject<Songs>()
            if(obj!=null){
                newSongList.add(obj)
            }
        }

        oldSongs.value=newSongList



    }

    fun isFavourite()= CoroutineScope(Dispatchers.IO).launch{

        val songId = currentSong?.id
        if (songId != null) {
            try {
                val db = Firebase.firestore.collection("songs").document(songId)
                val documentSnapshot = db.get().await()
                if (documentSnapshot.exists()) {
                    val isFavourite = documentSnapshot.getBoolean("isFavourite")
                    if (isFavourite != null) {
                        isFav.value=isFavourite
                    } else {
                        println("isFavourite field is null")
                    }
                } else {
                    println("Document does not exist")
                }
            } catch (e: Exception) {
                println("Error fetching isFavourite field: ${e.message}")
            }
        } else {
            println("currentSong is null")
        }








    }

    fun updateFavouriteField()= CoroutineScope(Dispatchers.IO).launch{
        val songId = currentSong?.id
        if (songId != null) {
            try {
                val db = Firebase.firestore.collection("songs")
                db.document(songId).update("isFavourite", isFav.value).await()
                Log.d("MOHIT","Song Updated")
            } catch (e: Exception) {
                Log.d("MOHIT",e.toString())
            }
        } else {
            Log.d("MOHIT","Song Null")
        }





    }

    fun fetchFavouriteSongs()= CoroutineScope(Dispatchers.IO).launch {

        try {
            val querySnapshot=Firebase.firestore.collection("songs").whereEqualTo("isFavourite",true).get().await()
            val lst= mutableListOf<Songs>()

            for(document in querySnapshot.documents){

                val song=document.toObject<Songs>()

                song?.let {
                    lst.add(song)
                }


            }

            songList.value=lst
        }
        catch (e:Exception){
            Log.d("MOHIT",e.toString())
        }


    }

}

