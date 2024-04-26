package com.example.sangeet.model

data class Songs(val id:String,val title:String,val subtitle:String
,val songUrl:String,val coverUrl:String,val isFavourite:Boolean){
    constructor():this("","","","","",false)
}
