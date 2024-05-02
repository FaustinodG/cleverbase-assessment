package com.faustinodegroot.secretmessaging.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faustinodegroot.secretmessaging.ConnectingView
import com.faustinodegroot.secretmessaging.CryptoView

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = BottomNavigationScreens.Connection.route
    ) {
        composable(BottomNavigationScreens.Connection.route) { ConnectingView() }
        composable(BottomNavigationScreens.Encryption.route) { CryptoView(true) }
        composable(BottomNavigationScreens.Decryption.route) { CryptoView(false) }
    }

}