package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.neki.app.R
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.Task
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgUnselectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.SemanticRed
import com.neki.app.ui.theme.SemanticYellow
import java.time.LocalDate

private val PixelFont = FontFamily(
    Font(R.font.pixelify_regular, FontWeight.Normal),
    Font(R.font.pixelify_medium, FontWeight.Medium),
    Font(R.font.pixelify_semibold, FontWeight.SemiBold),
    Font(R.font.pixelify_bold, FontWeight.Bold)
)

@Composable
fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .defaultMinSize(minHeight = 150.dp)
            .background(
                color = BgUnselectedElement,
                shape = RoundedCornerShape(0.dp)
            )
            .clipToBounds()
            .clickable {
                onEdit()
            }
    ) {
        TaskLeaf(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-10).dp, y = (-10).dp)
                .size(54.dp)
                .rotate(135f)
                .zIndex(0f)
        )

        TaskLeaf(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = (-10).dp)
                .size(54.dp)
                .rotate(225f)
                .zIndex(0f)
        )

        TaskLeaf(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-10).dp, y = 10.dp)
                .size(54.dp)
                .rotate(45f)
                .zIndex(0f)
        )

        TaskLeaf(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = 10.dp)
                .size(54.dp)
                .rotate(315f)
                .zIndex(0f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 58.dp,
                    end = 52.dp,
                    top = 30.dp,
                    bottom = 28.dp
                )
                .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = task.title,
                    color = DarkFont,
                    fontSize = 21.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (task.completed) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    }
                )

                PriorityPill(priority = task.priority)

                TaskMetaRow(task = task)
            }

            Spacer(modifier = Modifier.width(18.dp))

            Image(
                painter = painterResource(
                    id = if (task.completed) {
                        R.drawable.ic_checkbox_on
                    } else {
                        R.drawable.ic_checkbox
                    }
                ),
                contentDescription = "Marcar tarea como completada",
                colorFilter = ColorFilter.tint(IconGray),
                modifier = Modifier
                    .size(38.dp)
                    .clickable {
                        onToggleComplete()
                    },
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun TaskLeaf(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.task_card_leaf),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun PriorityPill(
    priority: Priority
) {
    Box(
        modifier = Modifier
            .background(
                color = priorityColor(priority),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 28.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = priorityLabel(priority),
            color = DkGreen,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TaskMetaRow(
    task: Task
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = "Fecha",
            colorFilter = ColorFilter.tint(IconGray),
            modifier = Modifier.size(18.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(7.dp))

        Text(
            text = formatTaskDate(task.dueDate),
            color = IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.width(22.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_bulletlist),
            contentDescription = "Subtareas",
            colorFilter = ColorFilter.tint(IconGray),
            modifier = Modifier.size(18.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(7.dp))

        Text(
            text = task.subTasks.size.toString(),
            color = IconGray,
            fontSize = 14.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Normal
        )
    }
}

private fun priorityColor(priority: Priority): Color {
    return when (priority.name.uppercase()) {
        "LOW" -> AlGreen
        "MEDIUM" -> AlGreen
        "HIGH" -> SemanticYellow
        "URGENT" -> SemanticRed
        else -> AlGreen
    }
}

private fun priorityLabel(priority: Priority): String {
    return when (priority.name.uppercase()) {
        "LOW" -> "Baja"
        "MEDIUM" -> "Media"
        "HIGH" -> "Alta"
        "URGENT" -> "Urgente"
        else -> priority.name
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