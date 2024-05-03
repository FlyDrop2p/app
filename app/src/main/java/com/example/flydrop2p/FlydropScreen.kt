package com.example.flydrop2p

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flydrop2p.ui.screen.Home.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flydrop2p.data.repository.ChatRepositoryImpl
import com.example.flydrop2p.data.repository.ChatsInfoRepositoryImpl
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
    chatName: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    TopAppBar(
        navigationIcon = {
            if (currentScreen != FlydropScreen.Home){
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        },
        title = {
            if (currentScreen == FlydropScreen.Chat) {
                Text(
                    text = chatName,
                    fontSize = 26.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (currentScreen == FlydropScreen.Home) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chat",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { /* Action for custom icon */ }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.wifi_tethering_24px),
                            contentDescription = "Settings",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* Action for settings icon */ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(currentScreen.title),
                    fontSize = 26.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        colors =  TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
        modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
    )

}

@Composable
fun FlydropApp(
    homeViewModel: HomeViewModel = HomeViewModel(ChatsInfoRepositoryImpl()),
    chatViewModel: ChatViewModel = ChatViewModel(ChatRepositoryImpl()),
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlydropScreen.valueOf(
        backStackEntry?.destination?.route ?: FlydropScreen.Home.name
    )

    val chatState = chatViewModel.uiState.collectAsState() // collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            FlydropAppBar(
                currentScreen = currentScreen,
                chatName = chatState.value.chat.name,
                navController = navController
            )
        }
    ){ innerPadding ->

        Log.d("FlydropApp", "ChatState: $chatState")


        NavHost(
            navController = navController,
            startDestination = FlydropScreen.Home.name,
            enterTransition = {
                EnterTransition.None
                // slideInHorizontally(animationSpec = tween(500))
            },
            exitTransition = {
                ExitTransition.None
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = FlydropScreen.Home.name) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium)),
                    homeViewModel = homeViewModel,
                    onChatClick = { chatId ->
                        chatViewModel.setChat(chatId)
                        navController.navigate(FlydropScreen.Chat.name)
                    }
                )
            }
            composable(route = FlydropScreen.Chat.name) {
                ChatScreen(
                    chatName = chatState.value.chat.name,
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