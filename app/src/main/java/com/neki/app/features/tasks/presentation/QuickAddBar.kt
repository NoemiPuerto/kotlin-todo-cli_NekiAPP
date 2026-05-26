package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.neki.app.R
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing

@Composable
fun QuickAddBar(
    taskTitle: String,
    onTaskTitleChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onExpandClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(NekiSpacing.md),
        shape = RoundedCornerShape(NekiRadius.lg),
        color = BgSelectedElement
    ) {
        Row(
            modifier = Modifier.padding(NekiSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(NekiSpacing.md)
        ) {
            IconButton(
                onClick = onExpandClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_plus_box),
                    contentDescription = "Expand task composer"
                )
            }

            OutlinedTextField(
                value = taskTitle,
                onValueChange = onTaskTitleChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Añadir nueva tarea")
                }
            )

            Button(
                onClick = onCreateClick
            ) {
                Text("Crear")
            }
        }
    }
}