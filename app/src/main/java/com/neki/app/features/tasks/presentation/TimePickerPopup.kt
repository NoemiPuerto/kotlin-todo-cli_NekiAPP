package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.neki.app.ui.components.NekiPopupCard
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.StrokeDiscardElement
import com.neki.app.ui.theme.Typography
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

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
    var selectedHour by remember(hour) { mutableIntStateOf(hour.coerceIn(1, 12)) }
    var selectedMinute by remember(minute) { mutableIntStateOf(minute.coerceIn(0, 59)) }
    var selectedIsAm by remember(isAm) { mutableStateOf(isAm) }

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        NekiPopupCard(modifier = Modifier.width(328.dp)) {
            Column(
                modifier = Modifier.height(286.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Selecciona hora",
                    color = DkGreen,
                    style = Typography.labelMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TimeWheel(
                        values = (1..12).toList(),
                        selectedValue = selectedHour,
                        labelFormatter = { "%02d".format(it) },
                        onValueSelected = { selectedHour = it }
                    )

                    Text(
                        text = ":",
                        color = DarkFont,
                        style = Typography.headlineLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    TimeWheel(
                        values = (0..59).toList(),
                        selectedValue = selectedMinute,
                        labelFormatter = { "%02d".format(it) },
                        onValueSelected = { selectedMinute = it }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    MeridiemSelector(
                        isAm = selectedIsAm,
                        onAmSelected = {
                            selectedIsAm = true
                            onAmSelected()
                        },
                        onPmSelected = {
                            selectedIsAm = false
                            onPmSelected()
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(NekiSpacing.xl)) {
                        Text(
                            text = "Cancelar",
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier.clickable { onDismiss() }
                        )

                        Text(
                            text = "OK",
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier.clickable {
                                if (selectedIsAm) onAmSelected() else onPmSelected()
                                onConfirm(selectedHour, selectedMinute)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeWheel(
    values: List<Int>,
    selectedValue: Int,
    labelFormatter: (Int) -> String,
    onValueSelected: (Int) -> Unit
) {
    val wheelValues = listOf<Int?>(null) + values + listOf<Int?>(null)
    val itemHeight = 44.dp
    val coroutineScope = rememberCoroutineScope()
    val selectedIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)

    LaunchedEffect(listState, values) {
        snapshotFlow { listState.firstVisibleItemIndex + 1 }
            .distinctUntilChanged()
            .collect { centerIndex ->
                wheelValues.getOrNull(centerIndex)?.let(onValueSelected)
            }
    }

    Box(
        modifier = Modifier
            .width(82.dp)
            .height(itemHeight * 3),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(RoundedCornerShape(16.dp))
                .background(BgSelectedElement.copy(alpha = 0.72f))
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .width(82.dp)
                .height(itemHeight * 3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(wheelValues) { index, value ->
                val centerIndex = listState.firstVisibleItemIndex + 1
                val isSelected = index == centerIndex && value != null

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable(enabled = value != null) {
                            if (value != null) {
                                onValueSelected(value)
                                coroutineScope.launch {
                                    listState.animateScrollToItem(values.indexOf(value).coerceAtLeast(0))
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (value != null) {
                        Text(
                            text = labelFormatter(value),
                            color = if (isSelected) DkGreen else IconGray.copy(alpha = 0.42f),
                            fontSize = if (isSelected) Typography.headlineMedium.fontSize else Typography.bodyLarge.fontSize,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
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
            .height(88.dp)
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
            .height(44.dp)
            .background(color = if (selected) AlGreen else Color.Transparent)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = DarkFont, style = Typography.labelMedium)
    }
}
