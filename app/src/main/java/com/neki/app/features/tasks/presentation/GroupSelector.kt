package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.neki.app.R
import com.neki.app.features.tasks.domain.TaskGroup
import com.neki.app.ui.theme.BgColor
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.NekiSpacing
import com.neki.app.ui.theme.StrokeDiscardElement
import com.neki.app.ui.theme.Typography

private val SearchPlaceholderColor = Color(0xFF9A9F8F)

@Composable
fun GroupSelector(
    selectedGroup: TaskGroup?,
    availableGroups: List<TaskGroup>,
    onGroupSelected: (TaskGroup) -> Unit,
    onCreateGroup: (String) -> TaskGroup
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredGroups = remember(searchQuery, availableGroups) {
        if (searchQuery.isBlank()) {
            availableGroups
        } else {
            availableGroups.filter {
                it.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }
    }

    val exactMatchExists = availableGroups.any {
        it.name.equals(
            searchQuery,
            ignoreCase = true
        )
    }

    Box(
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(
            modifier = Modifier
                .height(32.dp)
                .border(
                    width = 1.dp,
                    color = StrokeDiscardElement,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = BgColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                    expanded = true
                }
                .padding(horizontal = NekiSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(NekiSpacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = IconGray,
                        shape = RoundedCornerShape(999.dp)
                    )
                    .height(10.dp)
                    .padding(horizontal = 5.dp)
            )

            Text(
                text = selectedGroup?.name ?: "Inbox",
                color = DarkFont,
                style = Typography.labelMedium
            )

            Icon(
                painter = painterResource(R.drawable.ic_arrow_down_diamond),
                contentDescription = "Seleccionar grupo",
                tint = DarkFont
            )
        }

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(
                    focusable = true
                ),
                onDismissRequest = {
                    expanded = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .border(
                            width = 1.dp,
                            color = StrokeDiscardElement,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .background(
                            color = BgSelectedElement,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF0F0EB),
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            )
                            .padding(
                                horizontal = NekiSpacing.md,
                                vertical = NekiSpacing.md
                            )
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search For an option...",
                                color = SearchPlaceholderColor,
                                style = Typography.bodyMedium
                            )
                        }

                        BasicTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            singleLine = true,
                            textStyle = Typography.bodyMedium.copy(
                                color = DarkFont
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = "Select an option or create one",
                        color = IconGray,
                        style = Typography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = NekiSpacing.md,
                            vertical = NekiSpacing.sm
                        )
                    )

                    filteredGroups.forEach { group ->
                        Text(
                            text = group.name,
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onGroupSelected(group)
                                    expanded = false
                                    searchQuery = ""
                                }
                                .padding(
                                    horizontal = NekiSpacing.md,
                                    vertical = NekiSpacing.sm
                                )
                        )
                    }

                    if (
                        searchQuery.isNotBlank() &&
                        !exactMatchExists
                    ) {
                        Text(
                            text = "Crear \"$searchQuery\"",
                            color = DarkFont,
                            style = Typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newGroup = onCreateGroup(
                                        searchQuery
                                    )
                                    onGroupSelected(newGroup)
                                    expanded = false
                                    searchQuery = ""
                                }
                                .padding(
                                    horizontal = NekiSpacing.md,
                                    vertical = NekiSpacing.sm
                                )
                        )
                    }
                }
            }
        }
    }
}