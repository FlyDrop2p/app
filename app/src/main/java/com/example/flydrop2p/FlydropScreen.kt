package com.example.flydrop2p

import androidx.annotation.StringRes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.flydrop2p.ui.screen.Home.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flydrop2p.domain.model.ChatInfo
import com.example.flydrop2p.ui.screen.Chat.ChatScreen
import com.example.flydrop2p.ui.screen.Chat.ChatViewModel
import com.example.flydrop2p.ui.screen.Home.HomeScreen

enum class FlydropScreen(@StringRes val title: Int) {
    Home(R.string.app_name),
    Chat(R.string.chat_screen)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlydropAppBar(
    currentScreen: FlydropScreen,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        modifier = modifier,
    )

}

@Composable
fun FlydropApp(
    viewModel: HomeViewModel = viewModel(),
    chatViewModel: ChatViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlydropScreen.valueOf(
        backStackEntry?.destination?.route ?: FlydropScreen.Home.name
    )

    Scaffold(
    ){ innerPadding ->
        val chatInfoState = chatViewModel.uiState.collectAsState() // collectAsStateWithLifecycle()


        NavHost(
            navController = navController,
            startDestination = FlydropScreen.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = FlydropScreen.Home.name) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    onChatClick = { chatInfo ->
                        chatViewModel.setChatInfo(chatInfo)
                        navController.navigate(FlydropScreen.Chat.name)
                    }
                )
            }
            composable(route = FlydropScreen.Chat.name) {
                ChatScreen(
                    chatViewModel = chatViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

@Preview
@Composable
fun FlydropAppPreview(){
    FlydropApp()
}