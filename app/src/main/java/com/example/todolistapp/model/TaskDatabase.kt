package com.example.todolistapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolistapp.Convertors
import com.example.todolistapp.TaskDAO

@Database(entities = [ToDoItem::class], version = 1)
@TypeConverters(Convertors::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDAO():TaskDAO

    companion object{

        @Volatile
        private var INSTANCE:TaskDatabase ?=null

        fun getDatabase(context: Context):TaskDatabase{
            if(INSTANCE==null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "taskdb"
                    ).build()
                }
            }
            return INSTANCE!!
        }

    }
}