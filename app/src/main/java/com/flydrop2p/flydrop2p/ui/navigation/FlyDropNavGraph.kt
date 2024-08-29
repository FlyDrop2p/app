package com.flydrop2p.flydrop2p.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallScreen
import com.flydrop2p.flydrop2p.ui.screen.call.CallState
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
import com.flydrop2p.flydrop2p.ui.screen.info.InfoDestination
import com.flydrop2p.flydrop2p.ui.screen.info.InfoScreen
import com.flydrop2p.flydrop2p.ui.screen.info.InfoViewModel
import com.flydrop2p.flydrop2p.ui.screen.info.InfoViewModelFactory
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
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory())

            HomeScreen(
                homeViewModel = homeViewModel,
                navController = navController,
                onChatClick = { navController.navigate("${ChatDestination.route}/${it.accountId}") },
                onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
            )
        }

        composable(
            route = ChatDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatDestination.accountIdArg) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(ChatDestination.accountIdArg)
            accountId?.let {
                val chatViewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(accountId))

                ChatScreen(
                    accountId = accountId,
                    chatViewModel = chatViewModel,
                    navController = navController,
                    onInfoButtonClick = { navController.navigate("${InfoDestination.route}/${it}") },
                )
            }
        }

        composable(
            route = InfoDestination.routeWithArgs,
            arguments = listOf(navArgument(InfoDestination.accountIdArg) {
                type = NavType.LongType
            })
        ){backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(InfoDestination.accountIdArg)
            accountId?.let {
                val infoViewModel: InfoViewModel = viewModel(factory = InfoViewModelFactory(accountId))

                InfoScreen(
                    infoViewModel,
                    navController = navController,
                )
            }
        }

        composable(
            route = CallDestination.routeWithArgs,
            arguments = listOf(navArgument(CallDestination.accountIdArg) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong(CallDestination.accountIdArg)
            val callState = backStackEntry.arguments?.getString(CallDestination.callStateArg)?.let { CallState.valueOf(it) }

            if(accountId != null && callState != null) {
                BackHandler(true) {

                }

                val callViewModel: CallViewModel = viewModel(factory = CallViewModelFactory(accountId, callState))

                CallScreen(
                    callViewModel = callViewModel,
                    navController = navController
                )
            }
        }

        composable(
            route = SettingsDestination.route
        ) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory())

            SettingsScreen(
                settingsViewModel = settingsViewModel,
                navController = navController,
                onSettingsButtonClick = { navController.navigate(SettingsDestination.route) },
            )
        }
    }
}