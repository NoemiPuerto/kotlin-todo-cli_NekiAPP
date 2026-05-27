package com.neki.app.features.tasks.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neki.app.R
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.ui.theme.BgColor
import com.neki.app.ui.theme.IconGray
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.imePadding

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

    val today = remember {
        LocalDate.now(ZoneId.systemDefault())
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedCalendarDate by remember {
        mutableStateOf<LocalDate?>(null)
    }

    val filteredTasks = taskViewModel.tasks.filter { task ->
        val query = searchQuery.trim()

        val matchesSearch = query.isBlank() ||
                task.title.contains(query, ignoreCase = true) ||
                task.description.contains(query, ignoreCase = true) ||
                task.group?.name?.contains(query, ignoreCase = true) == true

        val taskDate = parseTaskDueDate(task.dueDate)

        val matchesDate = selectedCalendarDate == null ||
                taskDate == selectedCalendarDate

        matchesSearch && matchesDate
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 205.dp)
        ) {
            TaskTopSection(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                },
                today = today,
                activeDateFilter = selectedCalendarDate,
                onDateSelected = { date ->
                    selectedCalendarDate = if (selectedCalendarDate == date) {
                        null
                    } else {
                        date
                    }
                },
                modifier = Modifier.padding(top = 48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (filteredTasks.isEmpty()) {
                    EmptyTaskState(
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 18.dp
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredTasks) { task ->
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
                }
            }
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .imePadding()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 230.dp
                    )
                    .zIndex(2f)
            ) {
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
                isExpanded = false
            },
            onExpandClick = {
                isExpanded = !isExpanded
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .padding(bottom = 118.dp)
                .zIndex(1f)
        )

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
                onSave = { updatedTask ->
                    taskViewModel.updateTask(updatedTask)
                    taskViewModel.clearSelectedTask()
                },
                onDelete = {
                    taskViewModel.deleteTask(selectedTask.id)
                    taskViewModel.clearSelectedTask()
                },
                onDismiss = {
                    taskViewModel.clearSelectedTask()
                }
            )
        }
    }
}

@Composable
private fun EmptyTaskState(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_task_plant")

    val plantScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.035f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "plant_scale"
    )

    val plantAlpha by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "plant_alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 90.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_empty_tasks_plant),
                contentDescription = "Empty tasks plant",
                modifier = Modifier
                    .size(285.dp)
                    .scale(plantScale)
                    .alpha(plantAlpha)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Add task",
                color = IconGray,
                fontSize = 16.sp
            )
        }
    }
}

private fun parseTaskDueDate(
    dueDate: String?
): LocalDate? {
    if (dueDate.isNullOrBlank()) return null

    return runCatching {
        LocalDate.parse(dueDate)
    }.getOrNull()
}