package com.connor.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.tasks.model.MainRepository
import com.connor.tasks.model.room.TasksEntity
import com.connor.tasks.type.TasksType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _dao = Channel<TasksType>()
    val dao = _dao.receiveAsFlow()

    fun sendTask(tasksType: TasksType) {
        viewModelScope.launch {
            _dao.send(tasksType)
        }
    }

    val loadTasks = repository.loadTasks()

    suspend fun insertTask(task: TasksEntity) = repository.insertTask(task)
}