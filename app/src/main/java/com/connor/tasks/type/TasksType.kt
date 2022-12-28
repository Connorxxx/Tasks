package com.connor.tasks.type

import com.connor.tasks.model.room.TasksEntity

sealed class TasksType {
    data class Insert(val task: TasksEntity): TasksType()
}

inline fun TasksType.onInsert(insert: (TasksEntity) -> Unit) {
    if (this is TasksType.Insert) insert(task)
}