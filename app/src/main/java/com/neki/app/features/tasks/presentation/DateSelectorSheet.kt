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
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.neki.app.ui.components.NekiDashedDivider
import com.neki.app.ui.components.NekiPrimaryButton
import com.neki.app.ui.components.NekiButtonSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.neki.app.ui.theme.Typography
import androidx.compose.ui.Alignment
import com.neki.app.ui.theme.BgColor
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.StrokeSelectedElement
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neki.app.features.tasks.presentation.NekiCalendar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.SheetValue

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

    val initialSelectedDate = remember(selectedDueDate, today) {
        parseSelectedDate(selectedDueDate, today)
    }

    var pendingSelectedDate by remember {
        mutableStateOf(initialSelectedDate)
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

    var selectedRepeatOption by remember {
        mutableStateOf(RepeatOption.NONE)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BgColor,
        dragHandle = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .height(6.dp)
                        .background(
                            color = AlGreen,
                            shape = RoundedCornerShape(999.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = NekiSpacing.lg)
        ) {Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
        ) {
            Text(
                text = "Fecha",
                color = DarkFont,
                style = Typography.headlineMedium
            )

            DateOption(
                label = "Hoy",
                selected = pendingSelectedDate == today,
                iconRes = R.drawable.ic_calendar,
                trailingText = shortDayName(today.dayOfWeek)
            ) {
                pendingSelectedDate = today
            }

            DateOption(
                label = "Mañana",
                selected = pendingSelectedDate == today.plusDays(1),
                iconRes = R.drawable.ic_sun,
                trailingText = shortDayName(today.plusDays(1).dayOfWeek)
            ) {
                pendingSelectedDate = today.plusDays(1)
            }

            DateOption(
                label = "Este fin de semana",
                selected = pendingSelectedDate == nextWeekend(today),
                iconRes = R.drawable.ic_sofa,
                trailingText = shortDayName(nextWeekend(today).dayOfWeek)
            ) {
                pendingSelectedDate = nextWeekend(today)
            }

            DateOption(
                label = "Próxima semana",
                selected = pendingSelectedDate == today.plusWeeks(1),
                iconRes = R.drawable.ic_arrow_right,
                trailingText = formatDisplayDate(today.plusWeeks(1))
            ) {
                pendingSelectedDate = today.plusWeeks(1)
            }

            DateOption(
                label = "Sin fecha",
                selected = pendingSelectedDate == null,
                iconRes = R.drawable.ic_annoyed
            ) {
                pendingSelectedDate = null
            }

            NekiDashedDivider()

            NekiCalendar(
                today = today,
                selectedDate = pendingSelectedDate,
                onDateSelected = { selected ->
                    pendingSelectedDate = selected
                }
            )

            DateAction(
                iconRes = R.drawable.ic_clock,
                label = "Añadir hora",
                trailingText = selectedTimeLabel(
                    hour,
                    minute,
                    isAm
                )
            ) {
                showTimePicker = true
            }

            NekiDashedDivider()

            DateAction(
                iconRes = R.drawable.ic_repeat,
                label = "Repetir",
                trailingText = repeatDisplayLabel(
                    selectedRepeatOption,
                    pendingSelectedDate
                )
            ) {
                showRepeatMenu = true
            }

            NekiDashedDivider()

            if (showRepeatMenu) {
                RepeatMenuPopup(
                    weeklyLabel = weeklyRepeatLabel(pendingSelectedDate),
                    monthlyLabel = monthlyRepeatLabel(pendingSelectedDate),
                    yearlyLabel = yearlyRepeatLabel(pendingSelectedDate),

                    onDismiss = {
                        showRepeatMenu = false
                    },

                    onDailyClick = {
                        selectedRepeatOption = RepeatOption.DAILY
                        onRepeatSelected(RepeatOption.DAILY)
                        showRepeatMenu = false
                    },

                    onWeeklyClick = {
                        selectedRepeatOption = RepeatOption.WEEKLY
                        onRepeatSelected(RepeatOption.WEEKLY)
                        showRepeatMenu = false
                    },

                    onWorkdaysClick = {
                        selectedRepeatOption = RepeatOption.WORKDAYS
                        onRepeatSelected(RepeatOption.WORKDAYS)
                        showRepeatMenu = false
                    },

                    onMonthlyClick = {
                        selectedRepeatOption = RepeatOption.MONTHLY
                        onRepeatSelected(RepeatOption.MONTHLY)
                        showRepeatMenu = false
                    },

                    onYearlyClick = {
                        selectedRepeatOption = RepeatOption.YEARLY
                        onRepeatSelected(RepeatOption.YEARLY)
                        showRepeatMenu = false
                    },

                    onCustomClick = {
                        selectedRepeatOption = RepeatOption.CUSTOM
                        onRepeatSelected(RepeatOption.CUSTOM)
                        showRepeatMenu = false
                    },

                    onClearClick = {
                        selectedRepeatOption = RepeatOption.NONE
                        onRepeatSelected(RepeatOption.NONE)
                        showRepeatMenu = false
                    }
                )
            }

            if (showTimePicker) {
                TimePickerPopup(
                    hour = hour,
                    minute = minute,
                    isAm = isAm,

                    onDismiss = {
                        showTimePicker = false
                    },

                    onAmSelected = {
                        isAm = true
                    },

                    onPmSelected = {
                        isAm = false
                    },

                    onConfirm = { selectedHour, selectedMinute ->
                        hour = selectedHour
                        minute = selectedMinute

                        val period = if (isAm) "AM" else "PM"

                        onTimeSelected(
                            "%02d:%02d %s".format(
                                selectedHour,
                                selectedMinute,
                                period
                            )
                        )

                        showTimePicker = false
                    }
                )
            }
        }
            Spacer(
                modifier = Modifier.height(NekiSpacing.md)
            )

            NekiPrimaryButton(
                text = "Guardar",
                size = NekiButtonSize.XL,
                onClick = {
                    onDateSelected(
                        pendingSelectedDate?.toString()
                    )
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun DateOption(
    label: String,
    iconRes: Int,
    trailingText: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) {
                    StrokeSelectedElement
                } else {
                    androidx.compose.ui.graphics.Color.Transparent
                },
                shape = RoundedCornerShape(NekiRadius.lg)
            )
            .background(
                color = if (selected) {
                    BgSelectedElement
                } else {
                    androidx.compose.ui.graphics.Color.Transparent
                },
                shape = RoundedCornerShape(NekiRadius.lg)
            )
            .clickable {
                onClick()
            }
            .padding(NekiSpacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(NekiSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = DarkFont
            )

            Text(
                text = label,
                color = DarkFont,
                style = Typography.bodyLarge
            )
        }

        if (trailingText != null) {
            Text(
                text = trailingText,
                color = DarkFont.copy(alpha = 0.6f),
                style = Typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DateAction(
    iconRes: Int,
    label: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = NekiSpacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(NekiSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = DarkFont
            )

            Text(
                text = label,
                color = DarkFont,
                style = Typography.bodyLarge
            )
        }

        if (trailingText != null) {
            Text(
                text = trailingText,
                color = DarkFont.copy(alpha = 0.6f),
                style = Typography.bodyMedium
            )
        }
    }
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

private fun selectedTimeLabel(
    hour: Int,
    minute: Int,
    isAm: Boolean
): String {
    val period = if (isAm) "AM" else "PM"

    return "%02d:%02d %s".format(
        hour,
        minute,
        period
    )
}

private fun repeatDisplayLabel(
    option: RepeatOption,
    selectedDate: LocalDate?
): String {
    return when (option) {
        RepeatOption.DAILY -> "Cada día"
        RepeatOption.WEEKLY -> weeklyRepeatLabel(selectedDate)
        RepeatOption.WORKDAYS -> "Cada día laborable"
        RepeatOption.MONTHLY -> monthlyRepeatLabel(selectedDate)
        RepeatOption.YEARLY -> yearlyRepeatLabel(selectedDate)
        RepeatOption.CUSTOM -> "Personalizada"
        RepeatOption.NONE -> "Repetir"
    }
}