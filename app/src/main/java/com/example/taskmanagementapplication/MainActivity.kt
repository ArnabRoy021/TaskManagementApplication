package com.example.taskmanagementapplication

import com.example.taskmanagementapplication.ui.SplashScreen
import com.example.taskmanagementapplication.ui.EditTaskScreen
import com.example.taskmanagementapplication.ui.AddTaskScreen
import com.example.taskmanagementapplication.ui.TaskDetailsScreen
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import com.example.taskmanagementapplication.state.TaskStore
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanagementapplication.model.Task
import com.example.taskmanagementapplication.model.TaskPriority
import com.example.taskmanagementapplication.model.TaskStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val taskStore = remember { TaskStore() }
                var showSplashScreen by remember { mutableStateOf(true) }
                var showAddTaskScreen by remember { mutableStateOf(false) }
                var selectedTaskId by remember {
                    mutableStateOf<Int?>(null)
                }
                var showEditTaskScreen by remember {
                    mutableStateOf(false)
                }

                when {

                    showSplashScreen -> {
                        SplashScreen(
                            onTimeout = {
                                showSplashScreen = false
                            }
                        )
                    }

                    showAddTaskScreen -> {
                        AddTaskScreen(
                            onBack = {
                                showAddTaskScreen = false
                            },
                            onSave = { newTask ->
                                val taskWithId = newTask.copy(
                                    id = taskStore.nextId()
                                )

                                taskStore.addTask(taskWithId)
                                showAddTaskScreen = false
                            }
                        )
                    }


                    showEditTaskScreen && selectedTaskId != null -> {

                        val taskToEdit = taskStore.findTask(selectedTaskId!!)

                        if (taskToEdit != null) {
                            EditTaskScreen(
                                task = taskToEdit,

                                onBack = {
                                    showEditTaskScreen = false
                                },

                                onSave = { updatedTask ->
                                    taskStore.updateTask(updatedTask)
                                    showEditTaskScreen = false
                                }
                            )
                        } else {
                            showEditTaskScreen = false
                            selectedTaskId = null
                        }
                    }


                    selectedTaskId != null -> {
                        val selectedTask = taskStore.findTask(selectedTaskId!!)

                        if (selectedTask != null) {
                            TaskDetailsScreen(
                                task = selectedTask,

                                onBack = {
                                    selectedTaskId = null
                                },

                                onEdit = {
                                    showEditTaskScreen = true
                                },

                                onDelete = {
                                    taskStore.deleteTask(selectedTask.id)
                                    selectedTaskId = null
                                },

                                onChangeStatus = { newStatus: TaskStatus ->
                                    taskStore.updateTask(
                                        selectedTask.copy(status = newStatus)
                                    )
                                }
                            )
                        } else {
                            selectedTaskId = null
                        }
                    }

                    else -> {
                        TaskListScreen(
                            store = taskStore,
                            onAddTask = {
                                showAddTaskScreen = true
                            },
                            onTaskClick = { taskId ->
                                selectedTaskId = taskId
                            }
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun TaskListScreen(store: TaskStore, onAddTask: () -> Unit, onTaskClick: (Int) -> Unit) {
    val tasks = store.tasks

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember {
        mutableStateOf<TaskStatus?>(null)
    }

    val filteredTasks = tasks.filter { task ->
        val matchesSearch =
            task.title.contains(searchQuery, ignoreCase = true) ||
                    task.employeeName.contains(searchQuery, ignoreCase = true)

        val matchesStatus =
            selectedStatus == null || task.status == selectedStatus

        matchesSearch && matchesStatus
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Task Manager",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF172554)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage your team's work in one place",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )

            Button(
                onClick = onAddTask,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add Task")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search tasks or employees") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All") }
                )

                FilterChip(
                    selected = selectedStatus == TaskStatus.PENDING,
                    onClick = {
                        selectedStatus = TaskStatus.PENDING
                    },
                    label = { Text("Pending") }
                )

                FilterChip(
                    selected = selectedStatus == TaskStatus.IN_PROGRESS,
                    onClick = {
                        selectedStatus = TaskStatus.IN_PROGRESS
                    },
                    label = { Text("In Progress") }
                )

                FilterChip(
                    selected = selectedStatus == TaskStatus.COMPLETED,
                    onClick = {
                        selectedStatus = TaskStatus.COMPLETED
                    },
                    label = { Text("Completed") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tasks (${filteredTasks.size})",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredTasks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No tasks found",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Try another search or status filter.",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredTasks,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onClick = {
                                onTaskClick(task.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF172554)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.description,
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Assigned to ${task.employeeName}",
                fontSize = 14.sp,
                color = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoBadge(
                    text = task.priority.name,
                    backgroundColor = priorityColor(task.priority)
                )

                Spacer(modifier = Modifier.width(8.dp))

                InfoBadge(
                    text = statusLabel(task.status),
                    backgroundColor = statusColor(task.status)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Due date: ${task.dueDate}",
                fontSize = 13.sp,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
fun InfoBadge(
    text: String,
    backgroundColor: Color
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

fun priorityColor(priority: TaskPriority): Color {
    return when (priority) {
        TaskPriority.LOW -> Color(0xFF64748B)
        TaskPriority.MEDIUM -> Color(0xFFD97706)
        TaskPriority.HIGH -> Color(0xFFDC2626)
    }
}

fun statusColor(status: TaskStatus): Color {
    return when (status) {
        TaskStatus.PENDING -> Color(0xFF64748B)
        TaskStatus.IN_PROGRESS -> Color(0xFF2563EB)
        TaskStatus.COMPLETED -> Color(0xFF15803D)
    }
}

fun statusLabel(status: TaskStatus): String {
    return when (status) {
        TaskStatus.PENDING -> "Pending"
        TaskStatus.IN_PROGRESS -> "In Progress"
        TaskStatus.COMPLETED -> "Completed"
    }
}
