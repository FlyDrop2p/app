package com.flydrop2p.flydrop2p

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flydrop2p.flydrop2p.ui.AppViewModelProvider
import com.flydrop2p.flydrop2p.ui.navigation.FlydropNavHost
import com.flydrop2p.flydrop2p.ui.screen.Chat.ChatViewModel
import com.flydrop2p.flydrop2p.ui.screen.Home.HomeViewModel


@Composable
fun FlydropApp(
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    chatViewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val chatState = chatViewModel.uiState.collectAsState() // collectAsStateWithLifecycle()


    Scaffold { innerPadding ->
        FlydropNavHost(
            navController = navController,
            homeViewModel = homeViewModel,
            chatViewModel = chatViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlydropTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        })
}

@Preview
@Composable
fun FlydropAppPreview(){
    FlydropApp()
}