package com.example.valentinesgarage.ui.theme.jobs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.valentinesgarage.domain.model.RepairJob

@Composable
fun ActiveJobsScreen(
    onJobClick: (String) -> Unit,
    viewModel: ActiveJobsViewModel = hiltViewModel()
) {
    val jobs by viewModel.activeJobs.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadActiveJobs()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(jobs) { job ->
            JobCard(job = job, onClick = { onJobClick(job.jobId) })
        }
    }
}

@Composable
fun JobCard(job: RepairJob, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Job #${job.jobId.takeLast(6)}", style = MaterialTheme.typography.titleMedium)
            Text("Status: ${job.status}", style = MaterialTheme.typography.bodyMedium)
            Text("Tasks: ${job.tasks.count { !it.completed }} remaining")
        }
    }
}