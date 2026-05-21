package com.example.valentinesgarage.ui.theme.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeReportScreen(
    viewModel: EmployeeReportViewModel = hiltViewModel()
) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()
    val selectedEmployeeId by viewModel.selectedEmployeeId.collectAsStateWithLifecycle()
    val report by viewModel.report.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadEmployees()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Employee Report") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Employee dropdown
            var expanded by remember { mutableStateOf(false) }
            val selectedEmployeeName = employees.find { it.id == selectedEmployeeId }?.name
                ?: "Select employee"

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedEmployeeName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    label = { Text("Mechanic") }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    employees.forEach { emp ->
                        DropdownMenuItem(
                            text = { Text(emp.name) },
                            onClick = {
                                viewModel.selectEmployee(emp.id)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Report content
            report?.let { r ->
                // If you have a separate loading state, add it; otherwise just show
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Employee: ${r.employee.name}", style = MaterialTheme.typography.titleMedium)
                        Text("Role: ${r.employee.role}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tasks Completed: ${r.completedTasks.size}", style = MaterialTheme.typography.bodyLarge)
                        Text("Trucks Serviced: ${r.servicedTruckCount}", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Avg Odometer at Check-in: ${r.averageOdometerAtCheckIn.roundToInt()} km",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (r.completedTasks.isNotEmpty()) {
                    Text("Completed tasks", style = MaterialTheme.typography.titleSmall)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(r.completedTasks) { task ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "🔧 ${task.taskDescription}",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                } else {
                    Text("No completed tasks yet.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}