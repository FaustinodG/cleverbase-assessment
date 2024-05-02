package com.faustinodegroot.core.model

data class Contact(
    val id: Int,
    val username: String,
    val connectedDate: Long,
    val publicKey: String
)