package com.example.dmorenomusicapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailScreen(albumId: Int, onBack: () -> Unit) {
    Text(text = "Detail Screen for Album ID: $albumId")
}
