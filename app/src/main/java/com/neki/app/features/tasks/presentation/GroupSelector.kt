package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neki.app.features.tasks.domain.TaskGroup
import com.neki.app.ui.theme.NekiRadius
import com.neki.app.ui.theme.NekiSpacing

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

    val filteredGroups = availableGroups.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    OutlinedButton(
        onClick = {
            expanded = true
            searchQuery = ""
        }
    ) {
        Text(
            text = selectedGroup?.name ?: "Seleccionar grupo"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ) {
        Surface(
            modifier = Modifier.width(280.dp),
            shape = RoundedCornerShape(NekiRadius.lg)
        ) {
            Column(
                modifier = Modifier.padding(NekiSpacing.md),
                verticalArrangement = Arrangement.spacedBy(NekiSpacing.sm)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Search for an option...")
                    },
                    singleLine = true
                )

                Text("Select an option or create")

                if (filteredGroups.isEmpty() && searchQuery.isNotBlank()) {
                    Text(
                        text = "Create \"$searchQuery\"",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val newGroup = onCreateGroup(searchQuery)

                                onGroupSelected(newGroup)
                                expanded = false
                            }
                            .padding(NekiSpacing.sm)
                    )
                } else {
                    filteredGroups.forEach { group ->
                        Text(
                            text = group.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onGroupSelected(group)
                                    expanded = false
                                }
                                .padding(NekiSpacing.sm)
                        )
                    }
                }
            }
        }
    }
}