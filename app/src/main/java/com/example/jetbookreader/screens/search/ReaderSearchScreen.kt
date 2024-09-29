package com.example.jetbookreader.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.jetbookreader.components.InputField
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.model.MBook


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderSearchScreen(navController: NavController) {
    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            navController = navController,
            showProfile = false
        ) {
            navController.popBackStack()
        }
    }) { PaddingValues ->
        Surface(modifier = Modifier.padding(PaddingValues)) {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            BookList(navController)
        }
    }
}

@Composable
fun BookList(navController: NavController) {
    val listOfBooks = listOf(
        MBook(id = "asdfg", title = "Hi Again", authors = "veekshith", notes = null),
        MBook(id = "asdfg", title = "Hello Again", authors = "veekshith", notes = null),
        MBook(id = "asdfg", title = "Yo Again", authors = "jake", notes = null),
        MBook(id = "asdfg", title = "Hello Ain", authors = "sam", notes = null),
        MBook(id = "asdfg", title = "Hello Again", authors = "veekshith", notes = null)
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp),
        contentPadding = PaddingValues(6.dp)
    ) {
        items(items = listOfBooks) { book ->
            BookRow(book, navController)

        }

    }
}

@Composable
fun BookRow(book: MBook, navController: NavController) {
    Card(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .height(100.dp)
        .padding(6.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl =
                "http://books.google.com/books/content?id=zmnhDAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column() {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Author:${book.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }
}

@Preview(showBackground = true)
@Composable
fun ReaderSearchScreenPreview() {
    val navController = rememberNavController()
    ReaderSearchScreen(navController = navController)
}