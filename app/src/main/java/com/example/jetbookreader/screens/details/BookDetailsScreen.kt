package com.example.jetbookreader.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.data.Resource
import com.example.jetbookreader.model.Item
import com.example.jetbookreader.navigation.ReaderScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(
    navController: NavController,
    bookId: String,
    viewmodel: BookDetailsViewmodel = hiltViewModel()
) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Book Details",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            showProfile = false,
            navController = navController,
        ) {
            navController.navigate(ReaderScreens.SearchScreen.name)
        }
    }) { PaddingValues ->
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

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewmodel.getBookInfo(bookId)
                }.value

                if (bookInfo.data == null) {
                    LinearProgressIndicator()
                } else {
                    ShowBookDetails(bookInfo, navController)
                }
            }

        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {

    val bookData = bookInfo.data?.volumeInfo

    val googleBookId = bookInfo.data?.id

    Card(
        modifier = Modifier.padding(34.dp),
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = bookData!!.imageLinks.thumbnail),
            contentDescription = "Book Image",
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp)
        )
    }

    Text(
        text = bookData?.title.toString(),
        style = MaterialTheme.typography.headlineMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )
    Text(text = "Authors: ${bookData?.authors}")
    Text(text = "Page Count: ${bookData?.pageCount}")
    Text(
        text = "Categories: ${bookData?.categories}",
        style = MaterialTheme.typography.labelLarge
    )
    Text(
        text = "Published: ${bookData?.publishedDate}",
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(modifier = Modifier.height(5.dp))


}
