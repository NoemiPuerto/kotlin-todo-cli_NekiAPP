package com.neki.app.features.tasks.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neki.app.R
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.ui.theme.NekiSpacing
import java.util.UUID

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
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (taskViewModel.tasks.isEmpty()) {
                EmptyTaskState(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(NekiSpacing.md),
                    modifier = Modifier.fillMaxSize()
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

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Add task",
                color = Color(0xFF6F7568),
                fontSize = 16.sp
            )
        }
    }
}