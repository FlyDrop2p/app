package com.flydrop2p.flydrop2p.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.ui.navigation.FlyDropNavHost
import java.io.File


@Composable
fun FlyDropApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold { innerPadding ->
        FlyDropNavHost(
            navController = navController,
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
    isSettingsScreen: Boolean,
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
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

        },
        actions = {
            IconButton(
                onClick = onConnectionButtonClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wifi_tethering_24px),
                    contentDescription = "Connection",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = onSettingsButtonClick,
                enabled = !isSettingsScreen
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = if (isSettingsScreen) Color.Gray else Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        modifier = modifier.padding(bottom = 8.dp)
    )
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onCallButtonClick: () -> Unit,
    onInfoButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    contactImageFileName: String? = null
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onInfoButtonClick)
            ) {
                val imageModifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)

                if (contactImageFileName != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = File(LocalContext.current.filesDir, contactImageFileName)),
                        contentDescription = "Immagine profilo",
                        modifier = imageModifier,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.account_circle_24px),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = "Immagine di default",
                        modifier = imageModifier
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        },
        actions = {

            IconButton(
                onClick = onCallButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Call",
                    modifier = Modifier.size(25.dp)
                )
            }
            IconButton(
                onClick = onInfoButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info",
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        modifier = modifier
    )
}

@Preview
@Composable
fun FlyDropAppPreview() {
    ChatTopAppBar(
        title = "Chat",
        canNavigateBack = true,
        onCallButtonClick = {},
        onInfoButtonClick = {},
        navigateUp = {}
    )
}