package com.faustinodegroot.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.faustinodegroot.core.database.dao.MessageDao
import com.faustinodegroot.core.database.entity.Message

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}