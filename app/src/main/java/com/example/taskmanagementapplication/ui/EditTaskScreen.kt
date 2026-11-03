package com.example.taskmanagementapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanagementapplication.model.Task
import com.example.taskmanagementapplication.model.TaskPriority
import com.example.taskmanagementapplication.model.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    task: Task,
    onBack: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember(task.id) {
        mutableStateOf(task.title)
    }

    var description by remember(task.id) {
        mutableStateOf(task.description)
    }

    var employeeName by remember(task.id) {
        mutableStateOf(task.employeeName)
    }

    var dueDate by remember(task.id) {
        mutableStateOf(task.dueDate)
    }

    var priority by remember(task.id) {
        mutableStateOf(task.priority)
    }

    var status by remember(task.id) {
        mutableStateOf(task.status)
    }

    var priorityExpanded by remember {
        mutableStateOf(false)
    }

    var statusExpanded by remember {
        mutableStateOf(false)
    }

    var submitted by remember {
        mutableStateOf(false)
    }

    val titleError = submitted && title.isBlank()
    val employeeError = submitted && employeeName.isBlank()

    val dateError = submitted && !isValidDate(dueDate)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Cancel")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "Update task information",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = titleError,
                supportingText = {
                    if (titleError) {
                        Text("Task title is required")
                    }
                }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = employeeName,
                onValueChange = { employeeName = it },
                label = { Text("Employee name *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = employeeError,
                supportingText = {
                    if (employeeError) {
                        Text("Employee name is required")
                    }
                }
            )

            OutlinedTextField(
                value = dueDate,
                onValueChange = { dueDate = it },
                label = { Text("Due date * (YYYY-MM-DD)") },
                placeholder = { Text("2026-11-05") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = dateError,
                supportingText = {
                    if (dateError) {
                        Text("Enter a valid date in YYYY-MM-DD format")
                    }
                }
            )

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = {
                    priorityExpanded = !priorityExpanded
                }
            ) {
                OutlinedTextField(
                    value = priority.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = priorityExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = {
                        priorityExpanded = false
                    }
                ) {
                    TaskPriority.values().forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name) },
                            onClick = {
                                priority = option
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = {
                    statusExpanded = !statusExpanded
                }
            ) {
                OutlinedTextField(
                    value = status.name.replace("_", " "),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = statusExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = {
                        statusExpanded = false
                    }
                ) {
                    TaskStatus.values().forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.name.replace("_", " "))
                            },
                            onClick = {
                                status = option
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        submitted = true

                        val validTitle = title.isNotBlank()
                        val validEmployee = employeeName.isNotBlank()

                        val validDate = isValidDate(dueDate)

                        if (validTitle && validEmployee && validDate) {
                            onSave(
                                task.copy(
                                    title = title.trim(),
                                    description = description.trim(),
                                    employeeName = employeeName.trim(),
                                    priority = priority,
                                    dueDate = dueDate.trim(),
                                    status = status
                                )
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Changes")
                }
            }

            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
