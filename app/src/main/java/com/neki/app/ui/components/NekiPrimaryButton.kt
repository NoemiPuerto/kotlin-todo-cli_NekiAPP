package com.neki.app.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.LgGreen
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.Typography
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

enum class NekiButtonSize {
    SMALL,
    BASE,
    XL
}

@Composable
fun NekiPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: NekiButtonSize = NekiButtonSize.BASE,
    enabled: Boolean = true
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = if (isPressed) {
        DkGreen
    } else {
        LgGreen
    }

    val width = when (size) {
        NekiButtonSize.SMALL -> 92.dp
        NekiButtonSize.BASE -> 99.dp
        NekiButtonSize.XL -> 370.dp
    }

    val height = when (size) {
        NekiButtonSize.SMALL -> 36.dp
        NekiButtonSize.BASE -> 40.dp
        NekiButtonSize.XL -> 50.dp
    }

    val textStyle = when (size) {
        NekiButtonSize.SMALL -> Typography.labelMedium
        NekiButtonSize.BASE -> Typography.labelMedium
        NekiButtonSize.XL -> Typography.bodyMedium
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(NekiRadius.lg),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = LgGreen.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        )
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}