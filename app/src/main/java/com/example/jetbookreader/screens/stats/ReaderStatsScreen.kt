package com.example.jetbookreader.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.model.MBook
import com.example.jetbookreader.screens.home.HomeScreenViewmodel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun ReaderStatsScreen(
    navController: NavController,
    viewModel: HomeScreenViewmodel = hiltViewModel()
) {

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {

            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            } else {
                emptyList()
            }

            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = "Person Icon"
                        )

                    }
                    Text(
                        text = "Hi, ${
                            currentUser?.email.toString().split("@")[0].toUpperCase(
                                Locale.ROOT
                            )
                        }"
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {

                    val readBookList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        } else {
                            emptyList()
                        }

                    val readingBooks = books.filter { mBook ->
                        (mBook.startedReading != null) && (mBook.finishedReading == null)
                    }

                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "Your Stats", style = MaterialTheme.typography.headlineMedium)
                        Divider()
                        Text(text = "You are reading:${readingBooks.size} books")
                        Text(text = "You have read:${readBookList.size} books so far")

                    }
                }
            }


        }
    }
}