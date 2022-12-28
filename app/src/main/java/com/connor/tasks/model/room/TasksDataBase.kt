package com.connor.tasks.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TasksEntity::class])
abstract class TasksDataBase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

//    companion object {
//        private var instance: TasksDataBase? = null
//
//        @Synchronized
//        fun getDataBase(context: Context): TasksDataBase {
//            instance?.let {
//                return it
//            }
//            return Room.databaseBuilder(context.applicationContext, TasksDataBase::class.java, "tasks_database").build().apply {
//                instance = this
//            }
//        }
//    }
}