package com.neki.app.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neki.app.core.navigation.BottomNavItem
import com.neki.app.ui.theme.BgBoxElements
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.NekiRadius

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = BgBoxElements,
        tonalElevation = NekiRadius.sm
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item) },
                icon = {
                    Text(
                        text = item.label,
                        color = DkGreen
                    )
                }
            )
        }
    }
}