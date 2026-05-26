package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.neki.app.R
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val mexicoLocale = Locale("es", "MX")
private val displayFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", mexicoLocale)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorSheet(
    onDismiss: () -> Unit,
    selectedDueDate: String?,
    onDateSelected: (String?) -> Unit,
    onTimeSelected: (String?) -> Unit,
    onRepeatSelected: (RepeatOption) -> Unit
) {
    val today = remember {
        LocalDate.now(ZoneId.systemDefault())
    }

    val selectedDate = remember(selectedDueDate, today) {
        parseSelectedDate(selectedDueDate, today)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var showRepeatMenu by remember {
        mutableStateOf(false)
    }

    var hour by remember {
        mutableStateOf(7)
    }

    var minute by remember {
        mutableStateOf(0)
    }

    var isAm by remember {
        mutableStateOf(true)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BgSelectedElement
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NekiSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(NekiSpacing.md)
        ) {
            Text(
                text = "Fecha",
                color = DarkFont
            )

            DateOption("Hoy") {
                onDateSelected(today.toString())
            }

            DateOption("Mañana") {
                onDateSelected(today.plusDays(1).toString())
            }

            DateOption("Este fin de semana") {
                onDateSelected(nextWeekend(today).toString())
            }

            DateOption("Próxima semana") {
                onDateSelected(today.plusWeeks(1).toString())
            }

            DateOption("Sin fecha") {
                onDateSelected(null)
            }

            HorizontalDivider()

            DateAction(
                iconRes = R.drawable.ic_clock,
                label = "Añadir hora"
            ) {
                showTimePicker = !showTimePicker
            }

            DateAction(
                iconRes = R.drawable.ic_repeat,
                label = "Repetir"
            ) {
                showRepeatMenu = true
            }

            if (showRepeatMenu) {
                RepeatMenuPopup(
                    weeklyLabel = weeklyRepeatLabel(selectedDate),
                    monthlyLabel = monthlyRepeatLabel(selectedDate),
                    yearlyLabel = yearlyRepeatLabel(selectedDate),

                    onDismiss = {
                        showRepeatMenu = false
                    },

                    onDailyClick = {
                        onRepeatSelected(RepeatOption.DAILY)
                        showRepeatMenu = false
                    },

                    onWeeklyClick = {
                        onRepeatSelected(RepeatOption.WEEKLY)
                        showRepeatMenu = false
                    },

                    onWorkdaysClick = {
                        onRepeatSelected(RepeatOption.WORKDAYS)
                        showRepeatMenu = false
                    },

                    onMonthlyClick = {
                        onRepeatSelected(RepeatOption.MONTHLY)
                        showRepeatMenu = false
                    },

                    onYearlyClick = {
                        onRepeatSelected(RepeatOption.YEARLY)
                        showRepeatMenu = false
                    },

                    onCustomClick = {
                        onRepeatSelected(RepeatOption.CUSTOM)
                        showRepeatMenu = false
                    },

                    onClearClick = {
                        onRepeatSelected(RepeatOption.NONE)
                        showRepeatMenu = false
                    }
                )
            }

            if (showTimePicker) {
                TimePickerInline(
                    hour = hour,
                    minute = minute,
                    isAm = isAm,
                    onHourIncrement = {
                        hour = if (hour == 12) 1 else hour + 1
                    },
                    onMinuteIncrement = {
                        minute = if (minute == 59) 0 else minute + 1
                    },
                    onAmSelected = {
                        isAm = true
                    },
                    onPmSelected = {
                        isAm = false
                    },
                    onConfirm = {
                        val period = if (isAm) "AM" else "PM"

                        onTimeSelected(
                            "%02d:%02d %s".format(hour, minute, period)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DateOption(
    label: String,
    onClick: () -> Unit
) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = NekiSpacing.sm),
        color = DarkFont
    )
}

@Composable
private fun DateAction(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = NekiSpacing.sm),
        horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label
        )

        Text(
            text = label,
            color = DarkFont
        )
    }
}

@Composable
private fun TimePickerInline(
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onHourIncrement: () -> Unit,
    onMinuteIncrement: () -> Unit,
    onAmSelected: () -> Unit,
    onPmSelected: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(NekiSpacing.md)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(NekiSpacing.md)
        ) {
            TimeBlock(
                value = "%02d".format(hour),
                onClick = onHourIncrement
            )

            Text(
                text = ":",
                color = DarkFont
            )

            TimeBlock(
                value = "%02d".format(minute),
                onClick = onMinuteIncrement
            )

            Column {
                OutlinedButton(
                    onClick = onAmSelected
                ) {
                    Text("AM")
                }

                OutlinedButton(
                    onClick = onPmSelected
                ) {
                    Text("PM")
                }
            }
        }

        OutlinedButton(
            onClick = onConfirm
        ) {
            Text("Guardar")
        }
    }
}

@Composable
private fun TimeBlock(
    value: String,
    onClick: () -> Unit
) {
    Text(
        text = value,
        modifier = Modifier
            .background(
                color = AlGreen,
                shape = RoundedCornerShape(NekiRadius.lg)
            )
            .clickable {
                onClick()
            }
            .padding(NekiSpacing.xxl),
        color = DkGreen
    )
}

private fun parseSelectedDate(
    selectedDueDate: String?,
    today: LocalDate
): LocalDate? {
    if (selectedDueDate == null) return null

    return runCatching {
        LocalDate.parse(selectedDueDate)
    }.getOrElse {
        when (selectedDueDate) {
            "Hoy" -> today
            "Mañana" -> today.plusDays(1)
            "Este fin de semana" -> nextWeekend(today)
            "Próxima semana" -> today.plusWeeks(1)
            else -> null
        }
    }
}

private fun formatDate(date: LocalDate): String {
    return date.format(displayFormatter)
}

internal fun friendlyDateLabel(
    dateString: String?,
    today: LocalDate
): String {
    if (dateString == null) return "Sin fecha"

    val date = parseSelectedDate(dateString, today) ?: return "Sin fecha"

    return when {
        date == today -> "Hoy"
        date == today.plusDays(1) -> "Mañana"
        else -> formatDisplayDate(date)
    }
}

private fun formatDisplayDate(date: LocalDate): String {
    return date.format(displayFormatter)
}

private fun nextWeekend(today: LocalDate): LocalDate {
    val daysUntilSaturday = when (today.dayOfWeek) {
        DayOfWeek.SATURDAY -> 0
        DayOfWeek.SUNDAY -> 6
        else -> DayOfWeek.SATURDAY.value - today.dayOfWeek.value
    }

    return today.plusDays(daysUntilSaturday.toLong())
}

private fun weeklyRepeatLabel(date: LocalDate?): String {
    if (date == null) return "Cada semana"

    return "Cada semana el ${shortDayName(date.dayOfWeek)}"
}

private fun monthlyRepeatLabel(date: LocalDate?): String {
    if (date == null) return "Cada mes"

    return "Cada mes el ${date.dayOfMonth}"
}

private fun yearlyRepeatLabel(date: LocalDate?): String {
    if (date == null) return "Cada año"

    val month = date.month.getDisplayName(
        java.time.format.TextStyle.FULL,
        mexicoLocale
    )

    return "Cada año el ${date.dayOfMonth} de $month"
}

private fun shortDayName(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "lun"
        DayOfWeek.TUESDAY -> "mar"
        DayOfWeek.WEDNESDAY -> "mié"
        DayOfWeek.THURSDAY -> "jue"
        DayOfWeek.FRIDAY -> "vie"
        DayOfWeek.SATURDAY -> "sáb"
        DayOfWeek.SUNDAY -> "dom"
    }
}