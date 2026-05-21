package com.example.valentinesgarage.ui.theme.jobs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.valentinesgarage.domain.model.JobStatus
import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.model.RepairTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    jobId: String,
    viewModel: JobDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(jobId) {
        viewModel.loadJob(jobId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Repair Job") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Add new task dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.job != null -> {
                    JobContent(
                        job = uiState.job!!,
                        currentEmployeeName = uiState.currentEmployee?.name ?: "Mechanic",
                        onToggleTask = { taskId, notes ->
                            viewModel.toggleTaskCompleted(taskId, notes)
                        },
                        onAddNote = { taskId, note ->
                            viewModel.addNoteToTask(taskId, note)
                        }
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun JobContent(
    job: RepairJob,
    currentEmployeeName: String,
    onToggleTask: (taskId: String, notes: String) -> Unit,
    onAddNote: (taskId: String, note: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Job info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Job #${job.jobId.takeLast(6)}", style = MaterialTheme.typography.titleMedium)
                Text("Status: ${job.status}", style = MaterialTheme.typography.bodyMedium)
                if (job.overallNotes.isNotBlank()) {
                    Text("Overall notes: ${job.overallNotes}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Task list
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(job.tasks, key = { it.taskId }) { task ->
                TaskItem(
                    task = task,
                    isCurrentUserAssignee = task.completedByEmployeeName == currentEmployeeName,
                    onToggle = { notes ->
                        if (!task.completed) {
                            onToggleTask(task.taskId, notes)
                        }
                    },
                    onAddNote = { note ->
                        onAddNote(task.taskId, note)
                    }
                )
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: RepairTask,
    isCurrentUserAssignee: Boolean,
    onToggle: (notes: String) -> Unit,
    onAddNote: (note: String) -> Unit
) {
    var showNoteDialog by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.completed) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { checked ->
                    if (checked && !task.completed) {
                        // Open a dialog to capture completion notes
                        showNoteDialog = true
                    }
                },
                enabled = !task.completed
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(task.description, style = MaterialTheme.typography.bodyLarge)
                if (task.completed) {
                    Text(
                        text = "✓ Completed by ${task.completedByEmployeeName ?: "Unknown"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (task.notes.isNotBlank()) {
                    Text(
                        text = "📝 ${task.notes.take(80)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = { showNoteDialog = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Add note")
            }
        }
    }

    // Dialog for adding a note (and optionally completing)
    if (showNoteDialog) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            title = { Text(if (!task.completed) "Complete task" else "Add note") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Notes (optional)") },
                    minLines = 2,
                    maxLines = 4
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (!task.completed) {
                            onToggle(noteText)
                        } else {
                            onAddNote(noteText)
                        }
                        showNoteDialog = false
                        noteText = ""
                    }
                ) {
                    Text(if (!task.completed) "Mark completed" else "Add note")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}