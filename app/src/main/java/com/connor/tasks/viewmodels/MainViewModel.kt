package com.connor.tasks.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connor.tasks.model.MainRepository
import com.connor.tasks.model.room.TasksEntity
import com.connor.tasks.type.TasksType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    var text by mutableStateOf("")

    private val _dao = Channel<TasksType>()
    val dao = _dao.receiveAsFlow().flowOn(Dispatchers.IO)

    fun sendTask(tasksType: TasksType) {
        viewModelScope.launch(Dispatchers.Default) {
            _dao.send(tasksType)
        }
    }

    val loadTasks = repository.loadTasks().flowOn(Dispatchers.IO)

    suspend fun insertTask(task: TasksEntity) = repository.insertTask(task)
}