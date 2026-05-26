package com.neki.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.StrokeDiscardElement

@Composable
fun NekiPopupCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = BgSelectedElement,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            NekiRadius.xl
        ),
        border = BorderStroke(
            1.dp,
            StrokeDiscardElement
        )
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(
                horizontal = NekiSpacing.lg,
                vertical = NekiSpacing.xl
            ),
            content = content
        )
    }
}