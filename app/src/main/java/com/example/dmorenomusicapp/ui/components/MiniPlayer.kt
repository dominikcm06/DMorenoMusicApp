package com.example.dmorenomusicapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dmorenomusicapp.data.Album
import com.example.dmorenomusicapp.data.MusicApiService

@Composable
fun MiniPlayer() {
    var isPlaying by remember { mutableStateOf(false) }
    var album by remember { mutableStateOf<Album?>(null) }

    LaunchedEffect(Unit) {
        try {
            val service = MusicApiService.create()
            val albums = service.getAlbums()
            if (albums.isNotEmpty()) {
                album = albums.find { it.title == "Tales of Ithiria" } ?: albums[0]
            }
        } catch (e: Exception) {
        }
    }

    album?.let { currentAlbum ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF130E26))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentAlbum.image)
                        .addHeader("User-Agent", "Mozilla/5.0")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentAlbum.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Text(
                        text = currentAlbum.artist,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                Surface(
                    onClick = { isPlaying = !isPlaying },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color(0xFF130E26),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}
