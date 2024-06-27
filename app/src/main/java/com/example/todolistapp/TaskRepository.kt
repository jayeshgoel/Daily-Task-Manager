package com.example.todolistapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.todolistapp.model.ToDoItem
import java.util.Calendar
import java.util.Date

class TaskRepository(private val taskDAO: TaskDAO) {
    fun getTodayTask(date : Date):LiveData<List<ToDoItem>>{

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startDate:Date = calendar.time

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endDate:Date = calendar.time
        Log.d("UpdatedChanges","$startDate ---- $endDate")


        return taskDAO.getAllTasksForToday(startDate, endDate)

    }
    suspend fun insertTask(toDoItem: ToDoItem){
        taskDAO.insertTask(toDoItem)
    }
    suspend fun updateTask(toDoItem: ToDoItem){
        taskDAO.updateTask(toDoItem)
    }
    suspend fun delete(toDoItem: ToDoItem){
        taskDAO.deleteTask(toDoItem)
    }
}