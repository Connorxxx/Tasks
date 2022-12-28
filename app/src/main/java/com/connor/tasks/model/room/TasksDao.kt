package com.connor.tasks.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TasksEntity): Long

    @Query("select * from tasks")
    fun loadTasks(): Flow<List<TasksEntity>>
}