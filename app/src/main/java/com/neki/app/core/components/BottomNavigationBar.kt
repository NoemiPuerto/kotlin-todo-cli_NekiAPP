package com.neki.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.neki.app.core.navigation.BottomNavItem
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.DkGreen

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 10.dp,
                bottom = 14.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            BottomNavButton(
                item = item,
                isSelected = isSelected,
                onClick = {
                    onItemClick(item)
                }
            )
        }
    }
}

@Composable
private fun BottomNavButton(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    DkGreen
                } else {
                    BgBoxElements.copy(alpha = 0.92f)
                }
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = item.iconRes),
            contentDescription = item.label,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) {
                Color.White
            } else {
                DkGreen
            }
        )
    }
}