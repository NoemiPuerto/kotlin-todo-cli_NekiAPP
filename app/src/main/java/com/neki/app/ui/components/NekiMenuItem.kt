package com.neki.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.SemanticRed
import com.neki.app.ui.theme.Typography

@Composable
fun NekiMenuItem(
    text: String,
    onClick: () -> Unit,
    destructive: Boolean = false
) {
    Text(
        text = text,
        color = if (destructive) SemanticRed else DarkFont,
        style = Typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = NekiSpacing.sm)
    )
}