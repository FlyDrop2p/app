package com.flydrop2p.flydrop2p.ui.screen.settings

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.flydrop2p.flydrop2p.FlyDropTopAppBar
import com.flydrop2p.flydrop2p.R
import com.flydrop2p.flydrop2p.ui.navigation.NavigationDestination

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navController: NavHostController,
    onConnectionButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settingsState by settingsViewModel.uiState.collectAsState()

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
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.account_circle_24px),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Name: ${settingsState.profile.username}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Device: ${settingsState.profile.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            var usernameText by remember { mutableStateOf("") }
            TextField(
                value = usernameText,
                onValueChange = {
                    usernameText = it
                },
                label = { Text("Update Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    settingsViewModel.updateUsername(usernameText)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}