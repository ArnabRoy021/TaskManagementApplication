package com.example.taskmanagementapplication.data

import  com.example.taskmanagementapplication.model.Task
import  com.example.taskmanagementapplication.model.TaskPriority
import  com.example.taskmanagementapplication.model.TaskStatus

object SampleTasks {

    val tasks = listOf(
        Task(
            id = 1,
            title = "Update employee records",
            description = "Review and update employee information.",
            employeeName = "Rahul Sharma",
            priority = TaskPriority.HIGH,
            dueDate = "2026-10-30",
            status = TaskStatus.IN_PROGRESS
        ),
        Task(
            id = 2,
            title = "Prepare monthly report",
            description = "Compile the monthly performance report.",
            employeeName = "Akash Chopra",
            priority = TaskPriority.MEDIUM,
            dueDate = "2026-11-05",
            status = TaskStatus.PENDING
        ),
        Task(
            id = 3,
            title = "Review leave requests",
            description = "Review pending employee leave applications.",
            employeeName = "Ananya Singh",
            priority = TaskPriority.HIGH,
            dueDate = "2026-11-02",
            status = TaskStatus.PENDING
        ),
        Task(
            id = 4,
            title = "Organise team meeting",
            description = "Coordinate the weekly team meeting.",
            employeeName = "Simran Kaur",
            priority = TaskPriority.LOW,
            dueDate = "2026-11-05",
            status = TaskStatus.COMPLETED
        ),
        Task(
            id = 5,
            title = "Verify attendance records",
            description = "Check attendance entries for accuracy.",
            employeeName = "Vedanta Garg",
            priority = TaskPriority.MEDIUM,
            dueDate = "2026-11-03",
            status = TaskStatus.IN_PROGRESS
        ),
        Task(
            id = 6,
            title = "Prepare onboarding checklist",
            description = "Create a checklist for new employees.",
            employeeName = "Sabir Malik",
            priority = TaskPriority.HIGH,
            dueDate = "2026-11-04",
            status = TaskStatus.PENDING
        )
    )
}