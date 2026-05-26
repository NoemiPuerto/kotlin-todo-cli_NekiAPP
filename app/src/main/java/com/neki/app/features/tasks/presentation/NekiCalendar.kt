package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.neki.app.R
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun NekiCalendar(
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var visibleMonth by remember(selectedDate, today) {
        mutableStateOf(
            YearMonth.from(selectedDate ?: today)
        )
    }

    val days = remember(visibleMonth) {
        generateCalendarDays(visibleMonth)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CalendarHeader(
            visibleMonth = visibleMonth,
            onPreviousMonth = {
                visibleMonth = visibleMonth.minusMonths(1)
            },
            onNextMonth = {
                visibleMonth = visibleMonth.plusMonths(1)
            }
        )

        WeekdayHeader()

        CalendarGrid(
            days = days,
            today = today,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun CalendarHeader(
    visibleMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = visibleMonth.month.getDisplayName(
                java.time.format.TextStyle.FULL,
                Locale("es")
            ).replaceFirstChar { it.uppercase() } +
                    " " + visibleMonth.year,
            color = DkGreen,
            style = Typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onPreviousMonth()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left_box),
                    contentDescription = "Mes anterior",
                    tint = DkGreen,
                    modifier = Modifier.size(32.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onNextMonth()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Mes siguiente",
                    tint = DkGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun WeekdayHeader() {
    val weekdays = listOf("L", "M", "M", "J", "V", "S", "D")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        weekdays.forEach { day ->
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = DkGreen,
                    style = Typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    days: List<LocalDate?>,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    CalendarDay(
                        date = date,
                        today = today,
                        selectedDate = selectedDate,
                        onDateSelected = onDateSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate?,
    today: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    if (date == null) {
        Spacer(
            modifier = Modifier.size(44.dp)
        )
        return
    }

    val isSelected = date == selectedDate
    val isEnabled = !date.isBefore(today)

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = if (isSelected) AlGreen else Color.Transparent,
                shape = CircleShape
            )
            .clickable(
                enabled = isEnabled
            ) {
                onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = if (isEnabled) {
                DarkFont
            } else {
                DarkFont.copy(alpha = 0.3f)
            },
            style = Typography.bodyLarge
        )
    }
}

private fun generateCalendarDays(
    month: YearMonth
): List<LocalDate?> {
    val firstDay = month.atDay(1)

    val startOffset = when (firstDay.dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }

    val totalDays = month.lengthOfMonth()

    val result = mutableListOf<LocalDate?>()

    repeat(startOffset) {
        result.add(null)
    }

    repeat(totalDays) {
        result.add(month.atDay(it + 1))
    }

    while (result.size % 7 != 0) {
        result.add(null)
    }

    return result
}