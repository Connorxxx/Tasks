package com.connor.tasks.di

import android.content.Context
import androidx.room.Room
import com.connor.tasks.model.room.TasksDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTasksDatabase(
        @ApplicationContext ctx: Context
    ) = Room.databaseBuilder(
        ctx,
        TasksDataBase::class.java,
        "tasks_database"
    ).build()

    @Singleton
    @Provides
    fun provideTasksDao(dataBase: TasksDataBase) = dataBase.tasksDao()
}