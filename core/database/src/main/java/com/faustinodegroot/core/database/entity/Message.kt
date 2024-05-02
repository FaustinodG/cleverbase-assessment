package com.faustinodegroot.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val identifier: String,
    val encryptedMessage: String
)