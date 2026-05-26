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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.neki.app.ui.components.NekiDashedDivider
import com.neki.app.ui.theme.StrokeDiscardElement
import com.neki.app.ui.components.NekiActionChip
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.PathSegment
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.Typography

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NekiSpacing.md)
            .border(
                width = 1.dp,
                color = StrokeDiscardElement,
                shape = RoundedCornerShape(NekiRadius.xl)
            )
            .background(
                color = BgSelectedElement,
                shape = RoundedCornerShape(NekiRadius.xl)
            )
            .padding(NekiSpacing.md)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(NekiSpacing.md)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
            ) {
                NekiActionChip(
                    iconRes = R.drawable.ic_calendar,
                    label = friendlyDateLabel(
                        selectedDueDate,
                        LocalDate.now(ZoneId.systemDefault())
                    ),
                    onClick = onDateClick
                )

                NekiActionChip(
                    iconRes = R.drawable.ic_flag,
                    label = when (selectedPriority) {
                        Priority.LOW -> "Prioridad 4"
                        Priority.MEDIUM -> "Prioridad 3"
                        Priority.HIGH -> "Prioridad 2"
                        Priority.URGENT -> "Prioridad 1"
                    },
                    iconTint = priorityColor(selectedPriority),
                    onClick = {
                        priorityMenuExpanded = true
                    }
                )

                NekiActionChip(
                    iconRes = R.drawable.ic_bell,
                    label = "Notificar",
                    onClick = {
                        // TODO
                    }
                )
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



            NekiDashedDivider()

            Text(
                text = "Sub tareas"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (subTaskInput.isEmpty()) {
                        Text(
                            text = "Escribe sub tarea...",
                            color = DarkFont.copy(alpha = 0.5f),
                            style = Typography.labelMedium
                        )
                    }

                    BasicTextField(
                        value = subTaskInput,
                        onValueChange = {
                            subTaskInput = it
                        },
                        singleLine = true,
                        textStyle = _root_ide_package_.com.neki.app.ui.theme.Typography.labelMedium.copy(
                            color = DarkFont
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = AlGreen,
                            shape = RoundedCornerShape(999.dp),
                        )
                        .clickable {
                            if (subTaskInput.isNotBlank()) {
                                onAddSubTask(subTaskInput)
                                subTaskInput = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Agregar subtarea",
                        tint = DarkFont
                    )
                }
            }

            NekiDashedDivider()

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

            GroupSelector(
                selectedGroup = selectedGroup,
                availableGroups = availableGroups,
                onGroupSelected = onGroupSelected,
                onCreateGroup = onCreateGroup
            )
        }
    }
}