package com.example.jetbookreader.screens.update

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jetbookreader.R
import com.example.jetbookreader.components.InputField
import com.example.jetbookreader.components.RatingBar
import com.example.jetbookreader.components.ReaderAppBar
import com.example.jetbookreader.components.RoundedButton
import com.example.jetbookreader.data.DataOrException
import com.example.jetbookreader.model.MBook
import com.example.jetbookreader.navigation.ReaderScreens
import com.example.jetbookreader.screens.home.HomeScreenViewmodel
import com.example.jetbookreader.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

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
    }) { paddingValues ->

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
                .padding(paddingValues)
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

                    ShowSimpleForm(book = viewModel.data.value.data?.first { mBook ->
                        mBook.googleBookId == bookItemId
                    }!!, navController)

                }
            }

        }

    }
}

@Composable
fun ShowSimpleForm(book: MBook, navController: NavController) {

    val notesText = remember { mutableStateOf(book.notes ?: "") }
    val isStartReading = remember { mutableStateOf(false) }
    val isFinishedReading = remember { mutableStateOf(false) }
    val ratingVal = remember { mutableStateOf(0) }
    val context = LocalContext.current

    SimpleForm(
        modifier = Modifier,
        loading = false,
        defaultValue = book.notes.toString().ifEmpty { "No thoughts available." }
    ) { note ->

        notesText.value = note
    }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        TextButton(
            onClick = { isStartReading.value = true },
            enabled = book.startedReading == null
        ) {

            if (book.startedReading == null) {
                if (!isStartReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Started on :${formatDate(book.startedReading!!)}")

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        TextButton(
            onClick = { isFinishedReading.value = true },
            enabled = book.finishedReading == null
        ) {
            if (book.finishedReading == null) {
                if (!isFinishedReading.value) {
                    Text(text = "Mark as Read")
                } else {
                    Text(
                        text = "Reading Completed",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Read on :${formatDate(book.finishedReading!!)}")
            }
        }

    }
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    book.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingVal.value = rating
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))
    Row {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimestamp =
            if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimestamp =
            if (isStartReading.value) Timestamp.now() else book.startedReading
        val bookUpdate =
            changedNotes || changedRating || isStartReading.value || isFinishedReading.value
        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimestamp,
            "started_reading_at" to isStartedTimestamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()

        RoundedButton(label = "Update") {

            if (bookUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener { task ->
                        showToast(context, "Book Updated Successfully")
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)

                    }

            }


        }
        Spacer(modifier = Modifier.width(100.dp))
        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            ShowAlertDialog(title = "Delete Book",
                message = stringResource(id = R.string.sure) +
                        "\n" + stringResource(id = R.string.action), openDialog=openDialog
            ) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!).delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            openDialog.value = false
                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }
            }
        }
        RoundedButton(label = "Delete") {
            openDialog.value=true

        }
    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    message: String,
    openDialog: MutableState<Boolean>,
    onYesPressed: () -> Unit
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                TextButton(onClick = {
                    onYesPressed.invoke()
                    openDialog.value = false
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text(text = "No")
                }
            }
        )
    }
}


@Composable
fun SimpleForm(
    modifier: Modifier,
    loading: Boolean,
    defaultValue: String = "Great Book",
    onSearch: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) { textFieldValue.value.trim().isNotEmpty() }

        InputField(
            modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            isSingleLine = false,
            valueState = textFieldValue,
            labelId = "Enter your thoughts",
            enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )

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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
