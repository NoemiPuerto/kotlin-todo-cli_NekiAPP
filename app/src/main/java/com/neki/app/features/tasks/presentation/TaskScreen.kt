package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neki.app.ui.theme.NekiSpacing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.SubTask
import java.util.UUID
import com.neki.app.features.tasks.domain.RepeatOption

@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel = viewModel()
) {
    var taskTitle by remember { mutableStateOf("") }

    var isExpanded by remember { mutableStateOf(false) }

    var selectedPriority by remember {
        mutableStateOf(Priority.MEDIUM)
    }

    var selectedGroup by remember {
        mutableStateOf(taskViewModel.availableGroups.first())
    }

    var subTasks by remember {
        mutableStateOf(listOf<SubTask>())
    }

    var selectedDueDate by remember {
        mutableStateOf<String?>(null)
    }

    var showDateSheet by remember {
        mutableStateOf(false)
    }

    var selectedDueTime by remember {
        mutableStateOf<String?>(null)
    }

    var selectedRepeatOption by remember {
        mutableStateOf(RepeatOption.NONE)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(NekiSpacing.md),
            modifier = Modifier.weight(1f)
        ) {
            items(taskViewModel.tasks) { task ->
                TaskCard(
                    task = task,
                    onToggleComplete = {
                        taskViewModel.toggleTaskCompletion(task.id)
                    },
                    onDelete = {
                        taskViewModel.deleteTask(task.id)
                    },
                    onEdit = {
                        taskViewModel.selectTask(task)
                    }
                )
            }
        }

        QuickAddBar(
            taskTitle = taskTitle,
            onTaskTitleChange = {
                taskTitle = it
            },
            onCreateClick = {
                taskViewModel.addTask(
                    title = taskTitle,
                    priority = selectedPriority,
                    group = selectedGroup,
                    subTasks = subTasks,
                    dueDate = selectedDueDate,
                    dueTime = selectedDueTime,
                    repeatOption = selectedRepeatOption
                )
                taskTitle = ""
                subTasks = emptyList()
                selectedDueDate = null
                selectedDueTime = null
                selectedRepeatOption = RepeatOption.NONE
            },
            onExpandClick = {
                isExpanded = !isExpanded
            }
        )

        if (isExpanded) {
            ExpandedTaskComposer(
                selectedPriority = selectedPriority,
                selectedGroup = selectedGroup,
                availableGroups = taskViewModel.availableGroups,
                subTasks = subTasks,
                selectedDueDate = selectedDueDate,
                onPrioritySelected = {
                    selectedPriority = it
                },
                onGroupSelected = {
                    selectedGroup = it
                },
                onDateClick = {
                    showDateSheet = true
                },
                onCreateGroup = { groupName ->
                    taskViewModel.createGroup(groupName)
                },
                onAddSubTask = { title ->
                    subTasks = subTasks + SubTask(
                        id = UUID.randomUUID().toString(),
                        title = title
                    )
                },
                onRemoveSubTask = { subTask ->
                    subTasks = subTasks - subTask
                }
            )
        }

        if (showDateSheet) {
            DateSelectorSheet(
                onDismiss = {
                    showDateSheet = false
                },
                selectedDueDate = selectedDueDate,
                onDateSelected = { date ->
                    selectedDueDate = date
                },
                onTimeSelected = { time ->
                    selectedDueTime = time
                },
                onRepeatSelected = { repeat ->
                    selectedRepeatOption = repeat
                }
            )
        }

        taskViewModel.selectedTask?.let { selectedTask ->
            TaskEditorScreen(
                task = selectedTask,
                onSave = { newTitle ->
                    taskViewModel.updateTaskTitle(newTitle)
                    taskViewModel.clearSelectedTask()
                },
                onDismiss = {
                    taskViewModel.clearSelectedTask()
                }
            )
        }
    }
}