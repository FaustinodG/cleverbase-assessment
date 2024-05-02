package com.faustinodegroot.secretmessaging.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.faustinodegroot.secretmessaging.R

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object Connection : BottomNavigationScreens(
        "connection",
        R.string.bottom_navigation_connection,
        Icons.Filled.Share
    )

    data object Encryption : BottomNavigationScreens(
        "encryption",
        R.string.bottom_navigation_encryption,
        Icons.Filled.Lock
    )

    data object Decryption : BottomNavigationScreens(
        "decryption",
        R.string.bottom_navigation_decryption,
        Icons.Filled.Email
    )

}