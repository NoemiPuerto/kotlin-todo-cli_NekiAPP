package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.neki.app.R
import com.neki.app.features.tasks.domain.Attachment
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.TaskGroup
import com.neki.app.ui.components.NekiActionChip
import com.neki.app.ui.components.NekiDashedDivider
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.SemanticBlue
import com.neki.app.ui.theme.SemanticRed
import com.neki.app.ui.theme.SemanticYellow
import com.neki.app.ui.theme.StrokeDiscardElement
import com.neki.app.ui.theme.Typography
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
            Row(horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)) {
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
    attachments: List<Attachment>,
    onPrioritySelected: (Priority) -> Unit,
    onGroupSelected: (TaskGroup) -> Unit,
    onCreateGroup: (String) -> TaskGroup,
    onAddSubTask: (String) -> Unit,
    selectedDueDate: String?,
    onDateClick: () -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    onRemoveAttachment: (Attachment) -> Unit,
    onRemoveSubTask: (SubTask) -> Unit,
    onDismiss: () -> Unit,
) {
    var priorityMenuExpanded by remember { mutableStateOf(false) }
    var subTaskInput by remember { mutableStateOf("") }

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
        Column(verticalArrangement = Arrangement.spacedBy(NekiSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detalles",
                    color = DarkFont,
                    style = Typography.labelMedium
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(color = AlGreen, shape = RoundedCornerShape(999.dp))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Cerrar detalles",
                        tint = DarkFont,
                        modifier = Modifier
                            .size(22.dp)
                            .rotate(45f)
                    )
                }
            }

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
                    iconRes = R.drawable.ic_link,
                    label = when (attachments.size) {
                        0 -> "Adjunto"
                        1 -> "1 adjunto"
                        else -> "${attachments.size} adjuntos"
                    },
                    onClick = onAttachmentClick
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
                    onClick = { priorityMenuExpanded = true }
                )

                NekiActionChip(
                    iconRes = if (notificationsEnabled) R.drawable.ic_bell else R.drawable.ic_bell_off,
                    label = if (notificationsEnabled) "Notificando" else "Notificar",
                    onClick = onNotificationsClick
                )
            }

            DropdownMenu(
                expanded = priorityMenuExpanded,
                onDismissRequest = { priorityMenuExpanded = false }
            ) {
                PriorityMenuItem(Priority.URGENT, "Prioridad 1") {
                    onPrioritySelected(Priority.URGENT)
                    priorityMenuExpanded = false
                }
                PriorityMenuItem(Priority.HIGH, "Prioridad 2") {
                    onPrioritySelected(Priority.HIGH)
                    priorityMenuExpanded = false
                }
                PriorityMenuItem(Priority.MEDIUM, "Prioridad 3") {
                    onPrioritySelected(Priority.MEDIUM)
                    priorityMenuExpanded = false
                }
                PriorityMenuItem(Priority.LOW, "Prioridad 4") {
                    onPrioritySelected(Priority.LOW)
                    priorityMenuExpanded = false
                }
            }

            if (attachments.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 92.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(attachments, key = { it.id }) { attachment ->
                        CompactRemoveRow(
                            iconRes = R.drawable.ic_link,
                            text = attachment.name,
                            contentDescription = "Eliminar adjunto",
                            onRemove = { onRemoveAttachment(attachment) }
                        )
                    }
                }
            }

            NekiDashedDivider()

            Text(text = "Sub tareas", color = DarkFont, style = Typography.labelMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (subTaskInput.isEmpty()) {
                        Text(
                            text = "Escribe sub tarea...",
                            color = DarkFont.copy(alpha = 0.5f),
                            style = Typography.labelMedium
                        )
                    }

                    BasicTextField(
                        value = subTaskInput,
                        onValueChange = { subTaskInput = it },
                        singleLine = true,
                        textStyle = Typography.labelMedium.copy(color = DarkFont),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = AlGreen, shape = RoundedCornerShape(999.dp))
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

            if (subTasks.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 156.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(subTasks, key = { it.id }) { subTask ->
                        CompactRemoveRow(
                            iconRes = R.drawable.ic_bulletlist,
                            text = subTask.title,
                            contentDescription = "Eliminar subtarea",
                            onRemove = { onRemoveSubTask(subTask) }
                        )
                    }
                }
            }

            NekiDashedDivider()

            GroupSelector(
                selectedGroup = selectedGroup,
                availableGroups = availableGroups,
                onGroupSelected = onGroupSelected,
                onCreateGroup = onCreateGroup
            )
        }
    }
}

@Composable
private fun CompactRemoveRow(
    iconRes: Int,
    text: String,
    contentDescription: String,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.36f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = IconGray,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                color = DarkFont,
                style = Typography.labelMedium,
                maxLines = 1
            )
        }

        OutlinedButton(onClick = onRemove) {
            Icon(
                painter = painterResource(R.drawable.ic_trash),
                contentDescription = contentDescription,
                tint = IconGray,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
