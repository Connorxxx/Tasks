package com.connor.tasks.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TasksEntity(
    @PrimaryKey
    val id: Int,
    val task: String
)