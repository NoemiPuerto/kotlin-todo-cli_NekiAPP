package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.neki.app.ui.components.NekiPopupCard
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.StrokeDiscardElement
import com.neki.app.ui.theme.Typography
import androidx.compose.ui.window.PopupProperties

@Composable
fun TimePickerPopup(
    hour: Int,
    minute: Int,
    isAm: Boolean,
    onDismiss: () -> Unit,
    onAmSelected: () -> Unit,
    onPmSelected: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var hourText by remember {
        mutableStateOf("%02d".format(hour))
    }

    var minuteText by remember {
        mutableStateOf("%02d".format(minute))
    }

    LaunchedEffect(hour, minute) {
        hourText = "%02d".format(hour)
        minuteText = "%02d".format(minute)
    }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true
        )
    ) {
        NekiPopupCard(
            modifier = Modifier
                .width(328.dp)
        ) {
            Column(
                modifier = Modifier.height(226.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Selecciona hora",
                    color = DkGreen,
                    style = Typography.labelMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TimeInputField(
                        value = hourText,
                        onValueChange = {
                            if (it.length <= 2 && it.all(Char::isDigit)) {
                                hourText = it
                            }
                        },
                        backgroundColor = DkGreen.copy(alpha = 0.6f)
                    )

                    Text(
                        text = ":",
                        color = DarkFont,
                        style = Typography.headlineLarge
                    )

                    TimeInputField(
                        value = minuteText,
                        onValueChange = {
                            if (it.length <= 2 && it.all(Char::isDigit)) {
                                minuteText = it
                            }
                        },
                        backgroundColor = AlGreen
                    )

                    MeridiemSelector(
                        isAm = isAm,
                        onAmSelected = onAmSelected,
                        onPmSelected = onPmSelected
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            NekiSpacing.xl
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier.clickable {
                                onDismiss()
                            }
                        )

                        Text(
                            text = "OK",
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier.clickable {
                                val parsedHour =
                                    hourText.toIntOrNull()?.coerceIn(1, 12)
                                        ?: 7

                                val parsedMinute =
                                    minuteText.toIntOrNull()?.coerceIn(0, 59)
                                        ?: 0

                                onConfirm(parsedHour, parsedMinute)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    backgroundColor: Color
) {
    Row(
        modifier = Modifier
            .width(96.dp)
            .height(80.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(NekiRadius.lg)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                color = DarkFont,
                fontSize = Typography.headlineLarge.fontSize,
                fontWeight = Typography.headlineLarge.fontWeight,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            ),
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
private fun MeridiemSelector(
    isAm: Boolean,
    onAmSelected: () -> Unit,
    onPmSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(52.dp)
            .height(80.dp)
            .border(
                width = 1.dp,
                color = StrokeDiscardElement,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = BgBoxElements,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        MeridiemOption(
            text = "AM",
            selected = isAm,
            onClick = onAmSelected
        )

        MeridiemOption(
            text = "PM",
            selected = !isAm,
            onClick = onPmSelected
        )
    }
}

@Composable
private fun MeridiemOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = if (selected) AlGreen else Color.Transparent
            )
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = DarkFont,
            style = Typography.labelMedium
        )
    }
}