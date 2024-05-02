package com.faustinodegroot.core.database.di

import android.app.Application
import androidx.room.Room
import com.faustinodegroot.core.database.AppDatabase
import com.faustinodegroot.core.database.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "secret_message.db")
            .build()
    }

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }

}