package com.connor.tasks.model

import com.connor.tasks.model.room.TasksDao
import com.connor.tasks.model.room.TasksEntity
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val tasksDao: TasksDao
) {

    suspend fun insertTask(task: TasksEntity) = tasksDao.insertTask(task)

    fun loadTasks() = tasksDao.loadTasks()
}