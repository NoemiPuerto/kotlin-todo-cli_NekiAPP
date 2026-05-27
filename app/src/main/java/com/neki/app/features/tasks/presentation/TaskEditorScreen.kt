package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.neki.app.R
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgColor
import com.neki.app.ui.theme.BgUnselectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.SemanticRed
import java.time.LocalDate
import java.util.UUID

private val PixelFont = FontFamily(
    Font(R.font.pixelify_regular, FontWeight.Normal),
    Font(R.font.pixelify_medium, FontWeight.Medium),
    Font(R.font.pixelify_semibold, FontWeight.SemiBold),
    Font(R.font.pixelify_bold, FontWeight.Bold)
)

@Composable
fun TaskEditorScreen(
    task: Task,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember(task.id) {
        mutableStateOf(task.title)
    }

    var description by remember(task.id) {
        mutableStateOf(task.description)
    }

    var selectedPriority by remember(task.id) {
        mutableStateOf(task.priority)
    }

    var dueDate by remember(task.id) {
        mutableStateOf(task.dueDate)
    }

    var dueTime by remember(task.id) {
        mutableStateOf(task.dueTime)
    }

    var repeatOption by remember(task.id) {
        mutableStateOf(task.repeatOption)
    }

    var showDateSheet by remember {
        mutableStateOf(false)
    }

    val editableSubTasks = remember(task.id) {
        mutableStateListOf<SubTask>().apply {
            addAll(task.subTasks)
        }
    }

    var newSubTaskTitle by remember(task.id) {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 620.dp)
                .background(
                    color = BgColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Editar tarea",
                    color = DkGreen,
                    fontSize = 24.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.Bold
                )

                EditableField(
                    label = "Título",
                    value = title,
                    placeholder = "Nombre de la tarea",
                    singleLine = false,
                    onValueChange = {
                        title = it
                    }
                )

                EditableField(
                    label = "Descripción",
                    value = description,
                    placeholder = "Descripción",
                    singleLine = false,
                    onValueChange = {
                        description = it
                    }
                )

                Text(
                    text = "Prioridad",
                    color = IconGray,
                    fontSize = 14.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.Medium
                )

                PrioritySelector(
                    selectedPriority = selectedPriority,
                    onPrioritySelected = {
                        selectedPriority = it
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoButton(
                        label = "Fecha",
                        value = formatTaskDate(dueDate),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showDateSheet = true
                        }
                    )

                    InfoButton(
                        label = "Hora",
                        value = dueTime ?: "Sin hora",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showDateSheet = true
                        }
                    )
                }

                Text(
                    text = "Repetición",
                    color = IconGray,
                    fontSize = 14.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.Medium
                )

                RepeatSelector(
                    selectedRepeatOption = repeatOption,
                    onRepeatSelected = {
                        repeatOption = it
                    }
                )

                TaskInfoRow(
                    label = "Grupo",
                    value = task.group?.name ?: "Sin grupo"
                )

                Text(
                    text = "Subtareas",
                    color = IconGray,
                    fontSize = 14.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = BgUnselectedElement,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        BasicTextField(
                            value = newSubTaskTitle,
                            onValueChange = {
                                newSubTaskTitle = it
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = DarkFont,
                                fontSize = 14.sp,
                                fontFamily = PixelFont,
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (newSubTaskTitle.isBlank()) {
                                        Text(
                                            text = "Añadir subtarea",
                                            color = IconGray.copy(alpha = 0.45f),
                                            fontSize = 14.sp,
                                            fontFamily = PixelFont,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }

                                    innerTextField()
                                }
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = AlGreen,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                if (newSubTaskTitle.isNotBlank()) {
                                    editableSubTasks.add(
                                        SubTask(
                                            id = UUID.randomUUID().toString(),
                                            title = newSubTaskTitle.trim()
                                        )
                                    )
                                    newSubTaskTitle = ""
                                }
                            }
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Agregar",
                            color = DkGreen,
                            fontSize = 13.sp,
                            fontFamily = PixelFont,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                editableSubTasks.forEach { subTask ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = BgUnselectedElement,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = subTask.title,
                            color = DarkFont,
                            fontSize = 14.sp,
                            fontFamily = PixelFont,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "Quitar",
                            color = SemanticRed,
                            fontSize = 12.sp,
                            fontFamily = PixelFont,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                editableSubTasks.remove(subTask)
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EditorActionButton(
                        text = "Eliminar",
                        backgroundColor = BgUnselectedElement,
                        textColor = SemanticRed,
                        modifier = Modifier.weight(1f),
                        onClick = onDelete
                    )

                    EditorActionButton(
                        text = "Cancelar",
                        backgroundColor = BgUnselectedElement,
                        textColor = IconGray,
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    )

                    EditorActionButton(
                        text = "Guardar",
                        backgroundColor = AlGreen,
                        textColor = DkGreen,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    task.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        priority = selectedPriority,
                                        dueDate = dueDate,
                                        dueTime = dueTime,
                                        repeatOption = repeatOption,
                                        subTasks = editableSubTasks.toList()
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    if (showDateSheet) {
        DateSelectorSheet(
            onDismiss = {
                showDateSheet = false
            },
            selectedDueDate = dueDate,
            onDateSelected = { date ->
                dueDate = date
            },
            onTimeSelected = { time ->
                dueTime = time
            },
            onRepeatSelected = { repeat ->
                repeatOption = repeat
            }
        )
    }
}

@Composable
private fun EditableField(
    label: String,
    value: String,
    placeholder: String,
    singleLine: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Medium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = BgUnselectedElement,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = TextStyle(
                    color = DarkFont,
                    fontSize = 16.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                color = IconGray.copy(alpha = 0.45f),
                                fontSize = 16.sp,
                                fontFamily = PixelFont,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
private fun InfoButton(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = BgUnselectedElement,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = IconGray,
            fontSize = 13.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            color = DarkFont,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PriorityOption("Baja", Priority.LOW, selectedPriority, onPrioritySelected)
            PriorityOption("Media", Priority.MEDIUM, selectedPriority, onPrioritySelected)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PriorityOption("Alta", Priority.HIGH, selectedPriority, onPrioritySelected)
            PriorityOption("Urgente", Priority.URGENT, selectedPriority, onPrioritySelected)
        }
    }
}

@Composable
private fun PriorityOption(
    text: String,
    priority: Priority,
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    val selected = selectedPriority == priority

    Box(
        modifier = Modifier
            .background(
                color = if (selected) AlGreen else BgUnselectedElement,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable {
                onPrioritySelected(priority)
            }
            .padding(horizontal = 22.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) DkGreen else IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun RepeatSelector(
    selectedRepeatOption: RepeatOption,
    onRepeatSelected: (RepeatOption) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RepeatOptionPill("Ninguna", RepeatOption.NONE, selectedRepeatOption, onRepeatSelected)
            RepeatOptionPill("Diaria", RepeatOption.DAILY, selectedRepeatOption, onRepeatSelected)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RepeatOptionPill("Semanal", RepeatOption.WEEKLY, selectedRepeatOption, onRepeatSelected)
            RepeatOptionPill("Mensual", RepeatOption.MONTHLY, selectedRepeatOption, onRepeatSelected)
        }
    }
}

@Composable
private fun RepeatOptionPill(
    text: String,
    repeatOption: RepeatOption,
    selectedRepeatOption: RepeatOption,
    onRepeatSelected: (RepeatOption) -> Unit
) {
    val selected = selectedRepeatOption == repeatOption

    Box(
        modifier = Modifier
            .background(
                color = if (selected) AlGreen else BgUnselectedElement,
                shape = RoundedCornerShape(50.dp)
            )
            .clickable {
                onRepeatSelected(repeatOption)
            }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) DkGreen else IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
private fun TaskInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = BgUnselectedElement,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            color = DarkFont,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EditorActionButton(
    text: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 11.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

private fun formatTaskDate(
    dueDate: String?
): String {
    if (dueDate.isNullOrBlank()) {
        return "Sin fecha"
    }

    return try {
        val parsedDate = LocalDate.parse(dueDate)

        val month = when (parsedDate.monthValue) {
            1 -> "Ene"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Abr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Ago"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            else -> "Dic"
        }

        "$month ${parsedDate.dayOfMonth}"
    } catch (_: Exception) {
        dueDate
    }
}