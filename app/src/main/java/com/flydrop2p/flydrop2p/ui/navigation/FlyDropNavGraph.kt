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
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallScreen
import com.flydrop2p.flydrop2p.ui.screen.call.CallViewModel
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
    callViewModel: CallViewModel,
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
                type = NavType.LongType
            })

        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(ChatDestination.itemIdArg)
            accountId?.let {
                chatViewModel.collectContact(accountId)
                chatViewModel.collectMessages(accountId)
                ChatScreen(
                    accountId = it,
                    chatViewModel = chatViewModel,
                    navController = navController,
                    onConnectionButtonClick = onConnectionButtonClick,
                    onCallButtonClick = { navController.navigate("${CallDestination.route}/${it}") },
                )
            }
        }

        composable(
            route = CallDestination.routeWithArgs,
            arguments = listOf(navArgument(CallDestination.itemIdArg) {
                type = NavType.LongType
            })
        ){ backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(CallDestination.itemIdArg)
            accountId?.let {
                callViewModel.collectContact(accountId)
                callViewModel.startCall(accountId)

                CallScreen(
                    callViewModel = callViewModel,
                    navController = navController,
                    onHangUpClick = { navController.popBackStack() },
                    onSpeakerClick = { /*TODO*/ },
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