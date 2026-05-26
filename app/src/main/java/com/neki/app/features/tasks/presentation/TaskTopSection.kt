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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neki.app.R
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
            color = Color(0xFF48623F),
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
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Buscar tareas",
            colorFilter = ColorFilter.tint(Color(0xFF7D8478)),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xFF5E665A),
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
                            color = Color(0xFF9B9F98),
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
    var visibleWeekStart by remember {
        mutableStateOf(
            today.minusDays((today.dayOfWeek.value - 1).toLong())
        )
    }

    val days = remember(visibleWeekStart) {
        (0..6).map { index ->
            val date = visibleWeekStart.plusDays(index.toLong())

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

    val monthTitle = remember(visibleWeekStart) {
        val endOfWeek = visibleWeekStart.plusDays(6)

        if (visibleWeekStart.monthValue == endOfWeek.monthValue) {
            "${getSpanishMonthName(visibleWeekStart.monthValue)} ${visibleWeekStart.year}"
        } else {
            "${getSpanishMonthName(visibleWeekStart.monthValue)} - ${getSpanishMonthName(endOfWeek.monthValue)} ${endOfWeek.year}"
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "<",
                color = Color(0xFF48623F),
                fontSize = 22.sp,
                fontFamily = PixelFont,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    visibleWeekStart = visibleWeekStart.minusDays(7)
                }
            )

            Text(
                text = monthTitle,
                color = Color(0xFF48623F),
                fontSize = 16.sp,
                fontFamily = PixelFont,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = ">",
                color = Color(0xFF48623F),
                fontSize = 22.sp,
                fontFamily = PixelFont,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    visibleWeekStart = visibleWeekStart.plusDays(7)
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

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
}

@Composable
private fun CalendarDayItem(
    day: CalendarDay,
    selected: Boolean,
    isFiltering: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        Color(0xFFD7E9C3)
    } else {
        Color(0xFFE7E4DA)
    }

    val textColor = if (selected) {
        Color(0xFF48623F)
    } else {
        Color(0xFF777D72)
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

private fun getSpanishMonthName(month: Int): String {
    return when (month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        else -> "Diciembre"
    }
}

private data class CalendarDay(
    val letter: String,
    val number: String,
    val date: LocalDate
)