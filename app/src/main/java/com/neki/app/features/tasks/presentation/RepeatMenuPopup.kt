package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import com.neki.app.ui.components.NekiDashedDivider
import com.neki.app.ui.components.NekiMenuItem
import com.neki.app.ui.components.NekiPopupCard
import com.neki.app.ui.theme.NekiSpacing

@Composable
fun RepeatMenuPopup(
    weeklyLabel: String,
    monthlyLabel: String,
    yearlyLabel: String,
    onDismiss: () -> Unit,
    onDailyClick: () -> Unit,
    onWeeklyClick: () -> Unit,
    onWorkdaysClick: () -> Unit,
    onMonthlyClick: () -> Unit,
    onYearlyClick: () -> Unit,
    onCustomClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss
    ) {
        NekiPopupCard(
            modifier = Modifier.width(NekiSpacing.giant * 7)
        ) {
            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(
                    NekiSpacing.sm
                )
            ) {
                NekiMenuItem(
                    text = "Cada día",
                    onClick = onDailyClick
                )

                NekiMenuItem(
                    text = weeklyLabel,
                    onClick = onWeeklyClick
                )

                NekiMenuItem(
                    text = "Cada día laborable (lun a vie)",
                    onClick = onWorkdaysClick
                )

                NekiMenuItem(
                    text = monthlyLabel,
                    onClick = onMonthlyClick
                )

                NekiMenuItem(
                    text = yearlyLabel,
                    onClick = onYearlyClick
                )

                NekiMenuItem(
                    text = "Personalizada",
                    onClick = onCustomClick
                )

                NekiDashedDivider()

                NekiMenuItem(
                    text = "Borrar",
                    destructive = true,
                    onClick = onClearClick
                )
            }
        }
    }
}