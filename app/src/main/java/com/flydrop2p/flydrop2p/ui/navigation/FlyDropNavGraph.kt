package com.flydrop2p.flydrop2p.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatDestination
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatScreen
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeDestination
import com.flydrop2p.flydrop2p.ui.screen.home.HomeScreen
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsDestination
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsScreen
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel

@Composable
fun FlyDropNavHost(
    onConnectionButtonClick: () -> Unit,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        enterTransition = {
            EnterTransition.None
            // slideInHorizontally(animationSpec = tween(500))
        },
        exitTransition = {
            ExitTransition.None
        },
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            chatViewModel.resetChat()
            HomeScreen(
                homeViewModel = homeViewModel,
                chatViewModel = chatViewModel,
                onChatClick = { navController.navigate("${ChatDestination.route}/${it.accountId}") },
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
            )
        }

        composable(
            route = ChatDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatDestination.itemIdArg) {
                type = NavType.IntType
            })

        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getInt(ChatDestination.itemIdArg)
            accountId?.let {
                chatViewModel.collectContact(accountId)
                chatViewModel.collectMessages(accountId)
                ChatScreen(
                    accountId = it, // Passare direttamente l'ID della chat a ChatScreen
                    chatViewModel = chatViewModel,
                    navController = navController,
                    onConnectionButtonClick = onConnectionButtonClick,
                    onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
                )
            }
        }

        composable(
            route = SettingsDestination.route
        ) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                navController = navController,
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
            )
        }
    }
}