package com.example.jetbookreader.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.navigation.ReaderScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(navController: NavController, bookId: String) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            showProfile = false,
            navController = navController,
        ) {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) { PaddingValues->
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
                .padding(PaddingValues)
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Book id: $bookId")

            }
        }

    }
}