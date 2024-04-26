package com.example.sangeet.model

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.sangeet.MyViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object DataManager {

//
//    suspend fun fetchCategories(): List<Category> {
//        val db = FirebaseFirestore.getInstance()
//        val categories = mutableListOf<Category>()
//        val snapshot = db.collection("category").get().await()
//        for (doc in snapshot.documents) {
//            val name = doc.getString("name") ?: ""
//            val url = doc.getString("coverUrl") ?: ""
//            var songs= mutableListOf<String>()
//
//            if (doc.get("songs") is List<*>) {
//                @Suppress("UNCHECKED_CAST") // Suppress warning if you're sure it's a list
//                val stringList = doc.get("songs") as List<String>
//                // Use the list safely here
//               songs=stringList as MutableList<String>
//            } else {
//                // Handle the case where it's not a list
//            }
//
//
//
//
//            val category = Category(name, url,songs)
//            categories.add(category)
//        }
//        return categories
//    }
//
//
//
//   suspend fun fetchSongList(category: Category):List<Songs>{
//       val db = FirebaseFirestore.getInstance()
//       val data=db.collection("category").document(category.name).get()
//       var songList= mutableListOf<Songs>()
//
//
//
//
//
//              for (songId in category.songs) {
//                  val docRef = db.collection("songs").document(songId)
//                  docRef.get().addOnSuccessListener { documentSnapshot ->
//                      if (documentSnapshot.exists()) {
//
//                          val data = documentSnapshot.data
//                          val song = Songs( // Create a Song object with retrieved data
//
//
//
//
//                          )
//                          songList.add(song)
//                      } else {
//                          Log.d("Mohit", "Song document $songId not found")
//                      }
//                  }.addOnFailureListener { exception ->
//                      Log.d("Mohit", "Error getting song documents: ", exception)
//                  }
//              }
//
//
//
//
//
//
//
//return emptyList()
//
//   }
//
//
//
//
//
//
//


}


