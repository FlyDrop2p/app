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
import com.flydrop2p.flydrop2p.ui.screen.Chat.ChatDestination
import com.flydrop2p.flydrop2p.ui.screen.Chat.ChatScreen
import com.flydrop2p.flydrop2p.ui.screen.Chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.Home.HomeDestination
import com.flydrop2p.flydrop2p.ui.screen.Home.HomeScreen
import com.flydrop2p.flydrop2p.ui.screen.Home.HomeViewModel

@Composable
fun FlydropNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
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
                onChatClick = {
                    navController.navigate("${ChatDestination.route}/${it.id}") },
                homeViewModel = homeViewModel,
                chatViewModel = chatViewModel
            )
        }
        composable(
            route = ChatDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatDestination.itemIdArg) {
                type = NavType.IntType
            })

        ) {
                backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt(ChatDestination.itemIdArg)
            chatId?.let {
                chatViewModel.getChat(chatId)
                ChatScreen(
                    chatId = it, // Passare direttamente l'ID della chat a ChatScreen
                    chatViewModel = chatViewModel,
                    navController = navController
                )
            }
        }
    }
}