package com.example.dmorenomusicapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val image: String,
    val description: String = ""
)
