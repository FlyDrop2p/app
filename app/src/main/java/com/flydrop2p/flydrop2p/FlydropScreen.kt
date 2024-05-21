package com.flydrop2p.flydrop2p

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flydrop2p.flydrop2p.ui.AppViewModelProvider
import com.flydrop2p.flydrop2p.ui.navigation.FlyDropNavHost
import com.flydrop2p.flydrop2p.ui.screen.chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.home.HomeViewModel
import com.flydrop2p.flydrop2p.ui.screen.settings.SettingsViewModel


@Composable
fun FlyDropApp(
    onConnectionButtonClick: () -> Unit,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    chatViewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val chatState = chatViewModel.uiState.collectAsState() // collectAsStateWithLifecycle()


    Scaffold { innerPadding ->
        FlyDropNavHost(
            onConnectionButtonClick = onConnectionButtonClick,
            navController = navController,
            homeViewModel = homeViewModel,
            chatViewModel = chatViewModel,
            settingsViewModel = settingsViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlyDropTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {

    TopAppBar(
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onConnectionButtonClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.wifi_tethering_24px),
                        contentDescription = "Connection",
                        modifier = Modifier.size(36.dp)
                    )
                }
                IconButton(
                    onClick = onSettingsButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
        modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Preview
@Composable
fun FlyDropAppPreview() {
    FlyDropApp(onConnectionButtonClick = {})
}