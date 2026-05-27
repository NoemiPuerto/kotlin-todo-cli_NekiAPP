@file:Suppress("SpellCheckingInspection")

package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neki.app.R
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import java.time.LocalDate

private val PixelFont = FontFamily(
    Font(R.font.pixelify_regular, FontWeight.Normal),
    Font(R.font.pixelify_medium, FontWeight.Medium),
    Font(R.font.pixelify_semibold, FontWeight.SemiBold),
    Font(R.font.pixelify_bold, FontWeight.Bold)
)

@Composable
fun TaskTopSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    today: LocalDate,
    activeDateFilter: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        SearchTaskBar(
            value = searchQuery,
            onValueChange = onSearchQueryChange
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Hoy",
            color = DkGreen,
            fontSize = 24.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        MiniCalendar(
            today = today,
            activeDateFilter = activeDateFilter,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun SearchTaskBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(
                color = BgSelectedElement,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Buscar tareas",
            colorFilter = ColorFilter.tint(IconGray),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = DkGreen,
                fontSize = 14.sp,
                fontFamily = PixelFont,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = "Search tasks...",
                            color = IconGray.copy(alpha = 0.65f),
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
}

@Composable
private fun MiniCalendar(
    today: LocalDate,
    activeDateFilter: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val weekStart = remember(today) {
        today.minusDays((today.dayOfWeek.value - 1).toLong())
    }

    val days = remember(weekStart) {
        (0..6).map { index ->
            val date = weekStart.plusDays(index.toLong())

            CalendarDay(
                letter = when (date.dayOfWeek.value) {
                    1 -> "L"
                    2 -> "M"
                    3 -> "M"
                    4 -> "J"
                    5 -> "V"
                    6 -> "S"
                    else -> "D"
                },
                number = date.dayOfMonth.toString(),
                date = date
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            CalendarDayItem(
                day = day,
                selected = day.date == activeDateFilter || day.date == today && activeDateFilter == null,
                isFiltering = activeDateFilter != null,
                onClick = {
                    onDateSelected(day.date)
                }
            )
        }
    }
}

@Composable
private fun CalendarDayItem(
    day: CalendarDay,
    selected: Boolean,
    isFiltering: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        AlGreen
    } else {
        BgBoxElements
    }

    val textColor = if (selected) {
        DkGreen
    } else {
        IconGray
    }

    val itemAlpha = if (selected || !isFiltering) {
        1f
    } else {
        0.78f
    }

    Column(
        modifier = Modifier
            .size(width = 44.dp, height = 56.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = day.letter,
            color = textColor.copy(alpha = itemAlpha),
            fontSize = 13.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = day.number,
            color = textColor.copy(alpha = itemAlpha),
            fontSize = 12.sp,
            fontFamily = PixelFont,
            fontWeight = FontWeight.Normal
        )
    }
}

private data class CalendarDay(
    val letter: String,
    val number: String,
    val date: LocalDate
)
