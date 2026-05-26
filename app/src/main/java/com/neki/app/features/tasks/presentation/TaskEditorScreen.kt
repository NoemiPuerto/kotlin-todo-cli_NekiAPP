package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.neki.app.features.tasks.domain.Task
import com.neki.app.ui.theme.NekiSpacing

@Composable
fun TaskEditorScreen(
    task: Task,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(task.id) {
        mutableStateOf(task.title)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Edit Task")
        },

        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Task title")
                    }
                )
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    onSave(title)
                }
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}