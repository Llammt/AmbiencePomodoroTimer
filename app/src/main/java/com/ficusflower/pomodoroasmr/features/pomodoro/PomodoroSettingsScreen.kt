package com.ficusflower.pomodoroasmr.features.pomodoro
import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.audio.AmbientMode
import com.ficusflower.pomodoroasmr.domain.audio.AudioMode
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroConfig
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

@Composable
fun PomodoroSettingsScreen(
    onStartSession: (PomodoroConfig) -> Unit
) {
    var workMinutes by remember { mutableStateOf(25) }
    var shortBreakMinutes by remember { mutableStateOf(5) }
    var longBreakMinutes by remember { mutableStateOf(15) }

    var workAudioMode by remember { mutableStateOf<AudioMode>(AudioMode.SessionEndAlert) }
    var shortBreakAudioMode by remember { mutableStateOf<AudioMode>(AudioMode.SessionEndAlert) }
    var longBreakAudioMode by remember { mutableStateOf<AudioMode>(AudioMode.SessionEndAlert) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.pomodoro_settings_text_label),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        TimeSettingRow(label = stringResource(R.string.work_state_text_label), value = workMinutes, onValueChange = { workMinutes = it })
        SoundSelectionDropdown(
            modifier = Modifier.fillMaxWidth(),
            onModeSelected = { workAudioMode = it }
        )
        TimeSettingRow(label = stringResource(R.string.short_break_state_text_label), value = shortBreakMinutes, onValueChange = { shortBreakMinutes = it })
        SoundSelectionDropdown(
            modifier = Modifier.fillMaxWidth(),
            onModeSelected = { shortBreakAudioMode = it }
        )
        TimeSettingRow(label = stringResource(R.string.long_break_state_text_label), value = longBreakMinutes, onValueChange = { longBreakMinutes = it })
        SoundSelectionDropdown(
            modifier = Modifier.fillMaxWidth(),
            onModeSelected = { longBreakAudioMode = it }
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val config = PomodoroConfig(
                    workDurationMillis = workMinutes * 60 * 1000L,
                    shortBreakDurationMillis = shortBreakMinutes * 60 * 1000L,
                    longBreakDurationMillis = longBreakMinutes * 60 * 1000L,
                    workAudioMode = workAudioMode,
                    shortBreakAudioMode = shortBreakAudioMode,
                    longBreakAudioMode = longBreakAudioMode
                )
                onStartSession(config)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = stringResource(R.string.play_session_button_label),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 28.sp)
        }
    }
}

@Composable
fun TimeSettingRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > 1) onValueChange(value - 1) }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "-")
            }

            Text(
                text = "$value мин",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(onClick = { if (value < 60) onValueChange(value + 1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "+")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSelectionDropdown(
    modifier: Modifier = Modifier,
    onModeSelected: (AudioMode) -> Unit
) {
    val context = LocalContext.current
    val labelText = stringResource(R.string.ambient_sound_menu_text_label)
    val baseOptions = stringArrayResource(R.array.ambient_sound_list)
    val defaultCustomName = stringResource(R.string.custom_filename_text_label)

    var customSoundName by remember { mutableStateOf<String?>(null) } //TODO: move to ViewModel

    val options = remember(customSoundName) {
        val list = baseOptions.toMutableList()
        if (customSoundName != null && list.isNotEmpty()) {
            list[list.lastIndex] = customSoundName!!
        }
        list
    }

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(baseOptions[1]) }

    LaunchedEffect(Unit) {
        onModeSelected(mapIndexToAudioMode(1))
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                val uriString = uri.toString()
                val fileName = getFileNameFromUri(context, uri) ?: defaultCustomName

                customSoundName = fileName
                selectedOption = fileName

                onModeSelected(AudioMode.CustomAmbient(uriString))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val lightGreenField = Color(0xFFDCE5D8)
    val sandAccentMenu = Color(0xFFF7EFE0)

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOption,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightGreenField,
                    unfocusedContainerColor = lightGreenField,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth()
            )

            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                    surface = sandAccentMenu,
                    surfaceContainer = sandAccentMenu,
                    surfaceContainerHigh = sandAccentMenu
                )
            ) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                if (index == options.lastIndex) {
                                    expanded = false
                                    filePickerLauncher.launch(arrayOf("audio/*"))
                                } else {
                                    selectedOption = option
                                    expanded = false
                                    onModeSelected(mapIndexToAudioMode(index))
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
    }
}

private fun mapIndexToAudioMode(index: Int): AudioMode {
    return when (index) {
        0 -> AudioMode.Silence
        1 -> AudioMode.SessionEndAlert
        2 -> AudioMode.Ambient(AmbientMode.SpringForest)
        3 -> AudioMode.Ambient(AmbientMode.Water)

        else -> AudioMode.SessionEndAlert
    }
}

private fun getFileNameFromUri(context: android.content.Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}