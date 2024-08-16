package com.flydrop2p.flydrop2p.ui.screen.settings

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.flydrop2p.flydrop2p.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.random.Random

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings_screen
}

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navController: NavHostController,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settingsState by settingsViewModel.uiState.collectAsState()
    var usernameText by remember { mutableStateOf(settingsState.profile.username) }
    var profileImagePath by remember { mutableStateOf(settingsState.profile.imagePath ?: "") }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri ->
            val newImagePath = saveImageToInternalStorage(context = context, imageUri)
            newImagePath?.let {
                profileImagePath = it
                settingsViewModel.updateProfileImage(it)
            }
        }
    }

    fun generateRandomColor(): Color {
        return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
    }

    Scaffold(
        topBar = {
            FlyDropTopAppBar(
                title = "Settings",
                canNavigateBack = true,
                onConnectionButtonClick = onConnectionButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
                modifier = modifier,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
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
                            if (profileImagePath.isEmpty()) Color.Gray else generateRandomColor()
                        )
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                ) {
                    if (profileImagePath.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(profileImagePath)
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
                    text = "Name: $usernameText",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = usernameText,
                    onValueChange = {
                        usernameText = it
                    },
                    label = { Text("Update Username") },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.tertiaryContainer)
                )

                Button(
                    onClick = {
                        settingsViewModel.updateUsername(usernameText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFbae6fd)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save", color = Color(0xFF083249))
                }
            }
        }
    )
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
    val file = File(context.filesDir, "profile_image_${System.currentTimeMillis()}.jpg")

    return try {
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}
