package com.connor.tasks.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TasksEntity(
    val task: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}