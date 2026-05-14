package com.example.dmorenomusicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dmorenomusicapp.data.Album
import com.example.dmorenomusicapp.data.MusicApiService
import com.example.dmorenomusicapp.ui.components.MiniPlayer
import com.example.dmorenomusicapp.ui.theme.PrimaryPurple
import com.example.dmorenomusicapp.ui.theme.TextGray
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(albumId: String, onBack: () -> Unit) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun fetchDetail() {
        isLoading = true
        isError = false
        scope.launch {
            try {
                val service = MusicApiService.create()
                album = service.getAlbumById(albumId)
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                isError = true
            }
        }
    }

    LaunchedEffect(albumId) {
        fetchDetail()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else if (isError) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error al cargar el detalle", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { fetchDetail() }) {
                        Text("Reintentar")
                    }
                }
            }
        } else {
            album?.let { currentAlbum ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE9E3FF))
                ) {
                    DetailHeader(currentAlbum, onBack)
                    
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 110.dp, top = 8.dp)
                    ) {
                        item {
                            AboutSection(currentAlbum.description)
                        }
                        item {
                            ArtistChip(currentAlbum.artist)
                        }
                        itemsIndexed(List(10) { it }) { index, _ ->
                            TrackItem(currentAlbum, index + 1)
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            MiniPlayer()
        }
    }
}

@Composable
fun DetailHeader(album: Album, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.image)
                .addHeader("User-Agent", "Mozilla/5.0")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF2D1B69).copy(alpha = 0.85f)),
                        startY = 500f
                    )
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape).size(40.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            IconButton(
                onClick = { },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text(
                text = album.title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = album.artist,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryPurple,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Shuffle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutSection(description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0xFFD1C4E9)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "About this album",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF130E26)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = TextGray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ArtistChip(artist: String) {
    Surface(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(
                text = "Artist: ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF130E26)
            )
            Text(
                text = artist,
                fontSize = 13.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun TrackItem(album: Album, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp), spotColor = Color(0xFFD1C4E9)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.image)
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${album.title} • Track $index",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = album.artist,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
            Icon(
                Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}
