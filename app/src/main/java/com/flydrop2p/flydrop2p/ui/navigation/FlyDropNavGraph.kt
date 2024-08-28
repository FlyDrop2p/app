package com.flydrop2p.flydrop2p.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallScreen
import com.flydrop2p.flydrop2p.ui.screen.call.CallViewModel
import com.flydrop2p.flydrop2p.ui.screen.call.CallViewModelFactory
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatDestination
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatScreen
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModelFactory
import com.flydrop2p.flydrop2p.ui.screen.home.HomeDestination
import com.flydrop2p.flydrop2p.ui.screen.home.HomeScreen
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModelFactory
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsDestination
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsScreen
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModelFactory

@Composable
fun FlyDropNavHost(
    navController: NavHostController,
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
            val homeViewModelFactory = remember { HomeViewModelFactory() }
            val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)

            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController,
                onChatClick = { navController.navigate("${ChatDestination.route}/${it.accountId}") },
                onConnectionButtonClick = { homeViewModel.connect() },
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
                val chatViewModelFactory = remember(accountId) { ChatViewModelFactory(accountId) }
                val chatViewModel: ChatViewModel = viewModel(factory = chatViewModelFactory)

                ChatScreen(
                    accountId = it,
                    chatViewModel = chatViewModel,
                    navController = navController,
                    navigateToCallScreen = { navController.navigate("${CallDestination.route}/${it}") },
                    onInfoButtonClick = {} // TODO
                )
            }
        }

        composable(
            route = CallDestination.routeWithArgs,
            arguments = listOf(navArgument(CallDestination.itemIdArg) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(CallDestination.itemIdArg)
            accountId?.let {
                val callViewModelFactory = remember(accountId) { CallViewModelFactory(accountId) }
                val callViewModel: CallViewModel = viewModel(factory = callViewModelFactory)

                CallScreen(
                    callViewModel = callViewModel,
                    navController = navController,
                    onSpeakerClick = { /*TODO*/ },
                )
            }
        }

        composable(
            route = SettingsDestination.route
        ) {
            val settingsViewModelFactory = remember { SettingsViewModelFactory() }
            val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

            SettingsScreen(
                settingsViewModel = settingsViewModel,
                navController = navController,
                onConnectionButtonClick = { settingsViewModel.connect() },
                onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
            )
        }
    }
}