package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.TaskGroup
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.neki.app.R
import androidx.compose.ui.graphics.Color
import com.neki.app.ui.theme.SemanticRed
import com.neki.app.ui.theme.SemanticYellow
import com.neki.app.ui.theme.SemanticBlue
import com.neki.app.ui.theme.IconGray
import java.time.LocalDate
import java.time.ZoneId

private fun priorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.LOW -> IconGray
        Priority.MEDIUM -> SemanticBlue
        Priority.HIGH -> SemanticYellow
        Priority.URGENT -> SemanticRed
    }
}

@Composable
private fun PriorityMenuItem(
    priority: Priority,
    label: String,
    onSelect: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flag),
                    contentDescription = label,
                    tint = priorityColor(priority)
                )

                Text(label)
            }
        },
        onClick = onSelect
    )
}

@Composable
fun ExpandedTaskComposer(
    selectedPriority: Priority,
    selectedGroup: TaskGroup?,
    availableGroups: List<TaskGroup>,
    subTasks: List<SubTask>,
    onPrioritySelected: (Priority) -> Unit,
    onGroupSelected: (TaskGroup) -> Unit,
    onCreateGroup: (String) -> TaskGroup,
    onAddSubTask: (String) -> Unit,
    selectedDueDate: String?,
    onDateClick: () -> Unit,
    onRemoveSubTask: (SubTask) -> Unit,
) {
    var priorityMenuExpanded by remember {
        mutableStateOf(false)
    }

    var subTaskInput by remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NekiSpacing.md),
        shape = RoundedCornerShape(NekiRadius.lg),
        color = BgSelectedElement
    ) {
        Column(
            modifier = Modifier.padding(NekiSpacing.md),
            verticalArrangement = Arrangement.spacedBy(NekiSpacing.md)
        ) {
            OutlinedButton(
                onClick = {
                    priorityMenuExpanded = true
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flag),
                        contentDescription = "Prioridad",
                        tint = priorityColor(selectedPriority)
                    )

                    Text(
                        text = when (selectedPriority) {
                            Priority.LOW -> "Prioridad 4"
                            Priority.MEDIUM -> "Prioridad 3"
                            Priority.HIGH -> "Prioridad 2"
                            Priority.URGENT -> "Prioridad 1"
                        }
                    )
                }
            }

            DropdownMenu(
                expanded = priorityMenuExpanded,
                onDismissRequest = {
                    priorityMenuExpanded = false
                }
            ) {
                PriorityMenuItem(
                    priority = Priority.URGENT,
                    label = "Prioridad 1",
                    onSelect = {
                        onPrioritySelected(Priority.URGENT)
                        priorityMenuExpanded = false
                    }
                )

                PriorityMenuItem(
                    priority = Priority.HIGH,
                    label = "Prioridad 2",
                    onSelect = {
                        onPrioritySelected(Priority.HIGH)
                        priorityMenuExpanded = false
                    }
                )

                PriorityMenuItem(
                    priority = Priority.MEDIUM,
                    label = "Prioridad 3",
                    onSelect = {
                        onPrioritySelected(Priority.MEDIUM)
                        priorityMenuExpanded = false
                    }
                )

                PriorityMenuItem(
                    priority = Priority.LOW,
                    label = "Prioridad 4",
                    onSelect = {
                        onPrioritySelected(Priority.LOW)
                        priorityMenuExpanded = false
                    }
                )
            }

            GroupSelector(
                selectedGroup = selectedGroup,
                availableGroups = availableGroups,
                onGroupSelected = onGroupSelected,
                onCreateGroup = onCreateGroup
            )

            OutlinedButton(
                onClick = onDateClick
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar),
                        contentDescription = "Fecha"
                    )

                    Text(
                        text = friendlyDateLabel(
                            selectedDueDate,
                            LocalDate.now(ZoneId.systemDefault())
                        )
                    )
                }
            }

            Text("Sub tareas")

            Row(
                horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
            ) {
                OutlinedTextField(
                    value = subTaskInput,
                    onValueChange = {
                        subTaskInput = it
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text("Escribe sub tarea...")
                    },
                    singleLine = true
                )

                OutlinedButton(
                    onClick = {
                        if (subTaskInput.isNotBlank()) {
                            onAddSubTask(subTaskInput)
                            subTaskInput = ""
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Agregar subtarea"
                    )
                }
            }

            subTasks.forEach { subTask ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(subTask.title)

                    OutlinedButton(
                        onClick = {
                            onRemoveSubTask(subTask)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = "Eliminar subtarea"
                        )
                    }
                }
            }
        }
    }
}