package com.example.jetbookreader.screens.update

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.data.DataOrException
import com.example.jetbookreader.model.MBook
import com.example.jetbookreader.screens.home.HomeScreenViewmodel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ProduceStateDoesNotAssignValue")
@Composable
fun ReaderUpdateScreen(
    navController: NavController,
    bookItemId: String,
    viewModel: HomeScreenViewmodel = hiltViewModel()
) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Update Book",
            navController = navController,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            showProfile = false
        ) {
            navController.popBackStack()
        }
    }) { PaddingValues ->

        val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
            initialValue = DataOrException(
                data = emptyList(),
                true,
                Exception("")
            )
        ) {

            value = viewModel.data.value
        }.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
                .padding(PaddingValues)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false
                } else {

                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        shadowElevation = 4.dp
                    ) {

                        ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)

                    }

                }
            }

        }

    }
}

@Composable
fun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookItemId: String) {

    Row {
        Spacer(modifier = Modifier.width(43.dp))

        if (bookInfo.data != null) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center
            ) {

                CardListItem(book = bookInfo.data!!.first { mBook ->
                    mBook.googleBookId == bookItemId

                }, onPressDetails = {})

            }
        }

    }

}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberAsyncImagePainter(model = book.photoUrl.toString()),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .width(140.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 90.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
            Column {
                Text(
                    text = book.title.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 8.dp
                        )
                        .width(120.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp
                    )
                )
                Text(
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(
                        start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp
                    )
                )

            }
        }


    }
}
