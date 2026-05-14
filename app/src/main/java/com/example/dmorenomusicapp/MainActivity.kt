package com.example.dmorenomusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dmorenomusicapp.navigation.Detail
import com.example.dmorenomusicapp.navigation.Home
import com.example.dmorenomusicapp.ui.screens.DetailScreen
import com.example.dmorenomusicapp.ui.screens.HomeScreen
import com.example.dmorenomusicapp.ui.theme.DMorenoMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DMorenoMusicAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = Home
                        ) {
                            composable<Home> {
                                HomeScreen(
                                    onAlbumClick = { id ->
                                        navController.navigate(Detail(albumId = id))
                                    }
                                )
                            }
                            composable<Detail> { backStackEntry ->
                                val detail: Detail = backStackEntry.toRoute()
                                DetailScreen(
                                    albumId = detail.albumId,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
