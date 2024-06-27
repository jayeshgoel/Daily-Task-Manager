package com.example.todolistapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Time
import java.util.Date

@Entity(tableName = "Tasks")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    var heading:String,
    var description:String,
    var isChecked:Boolean,
    var date: Date
):Serializable