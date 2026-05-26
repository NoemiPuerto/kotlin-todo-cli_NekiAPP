package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neki.app.ui.theme.NekiSpacing
import androidx.compose.ui.Modifier

@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel = viewModel()
) {
    var taskTitle by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("New task")
            }
        )

        Button(
            onClick = {
                taskViewModel.addTask(taskTitle)
                taskTitle = ""
            }
        ) {
            Text("Add")
        }

        LazyColumn(
            contentPadding = PaddingValues(NekiSpacing.md)
        ) {
            items(taskViewModel.tasks) { task ->
                TaskCard(
                    task = task,
                    onToggleComplete = {
                        taskViewModel.toggleTaskCompletion(task.id)
                    },
                    onDelete = {
                        taskViewModel.deleteTask(task.id)
                    },
                    onEdit = {
                        taskViewModel.selectTask(task)
                    }
                )
            }
        }

        taskViewModel.selectedTask?.let { selectedTask ->
            TaskEditorScreen(
                task = selectedTask,
                onSave = { newTitle ->
                    taskViewModel.updateTaskTitle(newTitle)
                    taskViewModel.clearSelectedTask()
                },
                onDismiss = {
                    taskViewModel.clearSelectedTask()
                }
            )
        }
    }
}