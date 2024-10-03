package com.example.jetbookreader.screens.details

import androidx.lifecycle.ViewModel
import com.example.jetbookreader.data.Resource
import com.example.jetbookreader.model.Item
import com.example.jetbookreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewmodel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return repository.getBookInfo(bookId)
    }


}