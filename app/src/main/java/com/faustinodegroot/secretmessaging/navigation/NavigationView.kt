package com.faustinodegroot.secretmessaging.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faustinodegroot.secretmessaging.R
import com.faustinodegroot.secretmessaging.ui.theme.SecretMessagingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationView() {
    SecretMessagingTheme {
        val navHostController = rememberNavController()

        val bottomNavigationItems = listOf(
            BottomNavigationScreens.Connection,
            BottomNavigationScreens.Encryption,
            BottomNavigationScreens.Decryption
        )

        var title by remember { mutableStateOf<String?>(null) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        title?.let { Text(it) }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    currentDestination?.route?.let { route ->
                        title = stringResource(
                            id = when (route) {
                                BottomNavigationScreens.Connection.route -> BottomNavigationScreens.Connection.resourceId
                                BottomNavigationScreens.Encryption.route -> BottomNavigationScreens.Encryption.resourceId
                                BottomNavigationScreens.Decryption.route -> BottomNavigationScreens.Decryption.resourceId
                                else -> R.string.app_name
                            }
                        )
                    }

                    bottomNavigationItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = stringResource(item.resourceId)
                                )
                            },
                            label = { Text(stringResource(id = item.resourceId)) },
                            onClick = {
                                navHostController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navHostController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            })
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(navHostController = navHostController, modifier = Modifier.padding(innerPadding))
        }
    }
}