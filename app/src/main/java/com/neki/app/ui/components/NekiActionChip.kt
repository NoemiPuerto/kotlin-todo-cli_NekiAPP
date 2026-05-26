package com.neki.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.neki.app.ui.theme.AlGreen
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.Typography
import androidx.compose.ui.graphics.Color

@Composable
fun NekiActionChip(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    iconTint: Color = DarkFont
) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .background(
                color = AlGreen,
                shape = RoundedCornerShape(NekiRadius.md)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = NekiSpacing.md
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            tint = iconTint
        )

        Text(
            text = label,
            color = DarkFont,
            style = Typography.labelMedium
        )
    }
}