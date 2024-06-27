package com.example.todolistapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.model.ToDoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class MainViewModel(private val repository: TaskRepository):ViewModel() {
    private val _currentDate = MutableLiveData<Date>()
    val currentDate: LiveData<Date>
        get() = _currentDate

    init {
        _currentDate.value=Date()
    }
    fun setCurrentDate(date: Date) {
        _currentDate.value = date
    }

    fun getTodayTask(): LiveData<List<ToDoItem>>? {
        return _currentDate.switchMap {
            date -> repository.getTodayTask(date)
        }


    }
    fun insertTask(toDoItem: ToDoItem){
        viewModelScope.launch {
            repository.insertTask(toDoItem)
        }
    }
    fun updateTask(toDoItem: ToDoItem){
        viewModelScope.launch {
            repository.updateTask(toDoItem)
        }
    }
    fun deleteTask(toDoItem: ToDoItem){
        viewModelScope.launch {
            repository.delete(toDoItem)
        }
    }

}