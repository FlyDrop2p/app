package com.flydrop2p.flydrop2p.ui.screen.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.ui.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallDestination
import com.flydrop2p.flydrop2p.ui.screen.call.CallState
import java.io.File
import kotlin.random.Random

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navController: NavHostController,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val callRequest by settingsViewModel.networkManager.callRequest.collectAsState()

    LaunchedEffect(callRequest) {
        callRequest?.let {
            navController.navigate("${CallDestination.route}/${it.senderId}/${CallState.RECEIVED_CALL_REQUEST.name}")
        }
    }

    val context = LocalContext.current
    val settingsState by settingsViewModel.uiState.collectAsState()
    var usernameText by remember { mutableStateOf("") }
    val profileImageFileName = settingsState.profile.imageFileName ?: ""

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { imageUri ->
                settingsViewModel.updateProfileImage(imageUri)
            }
        }

    fun generateRandomColor(): Color {
        return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(settingsState.isSuccess) {
        if (settingsState.isSuccess) {
            snackbarHostState.showSnackbar("Operation successful!")
            settingsViewModel.setSuccess(false)
        }
    }

    LaunchedEffect(settingsState.errorMessage) {
        settingsState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            settingsViewModel.setError(null)
        }
    }

    Scaffold(
        topBar = {
            FlyDropTopAppBar(
                title = "Settings",
                canNavigateBack = true,
                isSettingsScreen = true,
                onConnectionButtonClick = { settingsViewModel.connect() },
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    contentColor = MaterialTheme.colorScheme.surface,
                    // actionColor = MaterialTheme.colorScheme.secondary,
                )
            },
        ) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            if (profileImageFileName.isEmpty()) Color.Gray else generateRandomColor()
                        )
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                ) {
                    if (profileImageFileName.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(File(context.filesDir, profileImageFileName))
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }

                Text(
                    text = settingsState.profile.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = usernameText,
                    onValueChange = {
                        usernameText = it
                    },
                    placeholder = {
                        Text(
                            "Update username",
                            fontSize = 14.sp,
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp
                    ),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        disabledLabelColor = Color.Transparent,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )

                Button(
                    onClick = {
                        settingsViewModel.updateUsername(usernameText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = usernameText.isNotBlank()
                ) {
                    Text("Save", color = MaterialTheme.colorScheme.surface)
                }

                Button(
                    onClick = {
                        settingsViewModel.backupMessages()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Backup messages", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Button(
                    onClick = {
                        settingsViewModel.retrieveMessages()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retrieve messages", color = MaterialTheme.colorScheme.surface)
                }
            }
        }
    )
}

