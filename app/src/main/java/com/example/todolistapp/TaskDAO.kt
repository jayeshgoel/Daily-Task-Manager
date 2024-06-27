package com.example.todolistapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todolistapp.model.ToDoItem
import java.util.Date

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(toDoItem: ToDoItem)

    @Update
    suspend fun updateTask(toDoItem: ToDoItem)

    @Delete
    suspend fun deleteTask(toDoItem: ToDoItem)

    @Query("SElECT * FROM Tasks WHERE Tasks.date>= :startDate AND Tasks.date<= :endDate")
    fun getAllTasksForToday(startDate: Date,endDate:Date): LiveData<List<ToDoItem>>


}