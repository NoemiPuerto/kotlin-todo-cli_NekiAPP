package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.NekiSpacing
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorSheet(
    onDismiss: () -> Unit,
    onDateSelected: (String?) -> Unit
) {
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
                onDateSelected("Hoy")
                onDismiss()
            }

            DateOption("Mañana") {
                onDateSelected("Mañana")
                onDismiss()
            }

            DateOption("Este fin de semana") {
                onDateSelected("Este fin de semana")
                onDismiss()
            }

            DateOption("Próxima semana") {
                onDateSelected("Próxima semana")
                onDismiss()
            }

            HorizontalDivider()

            DateOption("Sin fecha") {
                onDateSelected(null)
                onDismiss()
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