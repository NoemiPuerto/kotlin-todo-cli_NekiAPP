package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neki.app.features.tasks.domain.Task
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextButton

@Composable
fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(NekiSpacing.sm),
        shape = RoundedCornerShape(NekiRadius.lg),
        onClick = onToggleComplete
    ) {
        Column(
            modifier = Modifier.padding(NekiSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
        ) {
            Text(
                text = task.title,
                color = DarkFont,
                textDecoration = if (task.completed) {
                    TextDecoration.LineThrough
                } else {
                    null
                }
            )

            if (task.description.isNotBlank()) {
                Text(
                    text = task.description,
                    color = DarkFont
                )
            }

            Text(
                text = task.group?.name ?: "Sin grupo"
            )
            Row {
                TextButton(
                    onClick = onDelete
                ) {
                    Text("Delete")
                }
                TextButton(
                    onClick = onEdit
                ) {
                    Text("Edit")
                }
            }
        }
    }
}