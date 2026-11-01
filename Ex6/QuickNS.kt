package com.chris.neo.ex6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QuickNotesApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val database = remember { NoteDatabase.getDatabase(context) }
    val noteDao = remember { database.noteDao() }
    val themeRepository = remember { ThemeRepository(context) }

    val notes by noteDao.getAllNotes().collectAsStateWithLifecycle(initialValue = emptyList())
    val isDarkMode by themeRepository.darkModeFlow.collectAsStateWithLifecycle(initialValue = false)

    var newNoteText by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top App Title
                Text(
                    text = "Quick Notes",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Dark mode Switch row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dark mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { checked ->
                            coroutineScope.launch {
                                themeRepository.setDarkMode(checked)
                            }
                        }
                    )
                }

                // New note input & Add button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = { newNoteText = it },
                        label = { Text("New note") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            val trimmed = newNoteText.trim()
                            if (trimmed.isNotEmpty()) {
                                coroutineScope.launch(Dispatchers.IO) {
                                    noteDao.insertNote(Note(text = trimmed))
                                }
                                newNoteText = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }

                // Scrollable LazyColumn or Empty-State message
                if (notes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No notes yet. Add one above!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notes, key = { it.id }) { note ->
                            NoteItem(
                                note = note,
                                onDelete = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        noteDao.deleteNote(note)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = note.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            IconButton(onClick = onDelete) {
                Text(
                    text = "🗑",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}