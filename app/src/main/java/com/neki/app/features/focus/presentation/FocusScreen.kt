package com.neki.app.features.focus.presentation

import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neki.app.R
import com.neki.app.ui.theme.BgColor
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.BgUnselectedElement
import com.neki.app.ui.theme.DarkFont
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.LgGreen
import com.neki.app.ui.theme.NekiSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import com.neki.app.features.tasks.presentation.TaskViewModel

@Composable
fun FocusScreen(
    taskViewModel: TaskViewModel
) {
    var focusMinutes by rememberSaveable { mutableIntStateOf(25) }
    var breakMinutes by rememberSaveable { mutableIntStateOf(5) }
    var isBreak by rememberSaveable { mutableStateOf(false) }

    var remainingSeconds by rememberSaveable {
        mutableIntStateOf(focusMinutes * 60)
    }

    var isRunning by rememberSaveable { mutableStateOf(false) }
    var isBrownNoiseMuted by rememberSaveable { mutableStateOf(false) }
    var isSelectingTime by rememberSaveable { mutableStateOf(false) }

    val focusTasks = taskViewModel.tasks.filter { !it.completed }
    val taskTitles = focusTasks.map { it.title }.ifEmpty { listOf("Sin tarea seleccionada") }

    var selectedTaskId by rememberSaveable {
        mutableStateOf(focusTasks.firstOrNull()?.id)
    }

    val selectedFocusTask = focusTasks.firstOrNull { it.id == selectedTaskId }
    val selectedTask = selectedFocusTask?.title ?: "Sin tarea seleccionada"

    LaunchedEffect(focusTasks.map { it.id }) {
        if (selectedTaskId == null || focusTasks.none { it.id == selectedTaskId }) {
            selectedTaskId = focusTasks.firstOrNull()?.id
        }
    }

    var taskMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val toneGenerator = remember {
        ToneGenerator(AudioManager.STREAM_ALARM, 80)
    }

    val brownNoisePlayer = remember {
        MediaPlayer.create(context, R.raw.brown_noise).apply {
            isLooping = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (brownNoisePlayer.isPlaying) {
                brownNoisePlayer.stop()
            }

            brownNoisePlayer.release()
            toneGenerator.release()
        }
    }

    LaunchedEffect(isRunning, isBrownNoiseMuted) {
        if (isRunning && !isBrownNoiseMuted) {
            if (!brownNoisePlayer.isPlaying) {
                brownNoisePlayer.start()
            }
        } else {
            if (brownNoisePlayer.isPlaying) {
                brownNoisePlayer.pause()
            }
        }
    }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds == 0) {
            toneGenerator.startTone(
                ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,
                700
            )

            isBreak = !isBreak

            remainingSeconds = if (isBreak) {
                breakMinutes * 60
            } else {
                focusMinutes * 60
            }

            isRunning = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_focus),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = NekiSpacing.lg,
                    end = NekiSpacing.lg,
                    top = 70.dp,
                    bottom = NekiSpacing.lg
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color.White.copy(alpha = 0.72f))
                        .padding(NekiSpacing.lg),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TaskSelector(
                        selectedTask = selectedTask,
                        taskOptions = taskTitles,
                        expanded = taskMenuExpanded,
                        onExpandedChange = { taskMenuExpanded = it },
                        onTaskSelected = { taskTitle ->
                            selectedTaskId = focusTasks.firstOrNull { it.title == taskTitle }?.id
                        }
                    )

                    Spacer(modifier = Modifier.height(NekiSpacing.xl))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(165.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelectingTime) {
                            FocusMinutePicker(
                                selectedMinutes = focusMinutes,
                                onMinuteChanged = { minute ->
                                    focusMinutes = minute
                                    breakMinutes = getBreakMinutesForFocus(minute)
                                    isBreak = false
                                    isRunning = false
                                    remainingSeconds = minute * 60
                                },
                                onDone = {
                                    isSelectingTime = false
                                }
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = remainingSeconds.toPomodoroTime(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            isRunning = false
                                            isSelectingTime = true
                                        },
                                    color = DkGreen,
                                    fontSize = 76.sp,
                                    lineHeight = 76.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Tarea activa: $selectedTask",
                                    color = DarkFont,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(NekiSpacing.xl))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(NekiSpacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimerIconButton(
                            iconRes = R.drawable.ic_stop,
                            contentDescription = "Detener",
                            small = true,
                            onClick = {
                                isRunning = false
                                isBreak = false
                                isSelectingTime = false
                                remainingSeconds = focusMinutes * 60
                            }
                        )

                        TimerIconButton(
                            iconRes = R.drawable.ic_play,
                            contentDescription = "Iniciar o reanudar",
                            onClick = {
                                isSelectingTime = false
                                isRunning = true
                            }
                        )

                        TimerIconButton(
                            iconRes = R.drawable.ic_pause,
                            contentDescription = "Pausar",
                            small = true,
                            onClick = {
                                isRunning = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                MuteButton(
                    isMuted = isBrownNoiseMuted,
                    onClick = {
                        isBrownNoiseMuted = !isBrownNoiseMuted
                    }
                )

                selectedFocusTask?.takeIf { it.subTasks.isNotEmpty() }?.let { task ->
                    Spacer(modifier = Modifier.height(14.dp))
                    FocusSubTasksCard(
                        task = task,
                        onToggleSubTask = { subTask ->
                            taskViewModel.updateTask(
                                task.copy(
                                    subTasks = task.subTasks.map {
                                        if (it.id == subTask.id) it.copy(completed = !it.completed) else it
                                    }
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskSelector(
    selectedTask: String,
    taskOptions: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onTaskSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable {
                        onExpandedChange(true)
                    }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .clip(RoundedCornerShape(50))
                        .background(LgGreen)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Tarea" ,
                    color = DarkFont,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "⌄",
                    color = DarkFont,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    onExpandedChange(false)
                }
            ) {
                taskOptions.forEach { task ->
                    DropdownMenuItem(
                        text = {
                            Text(task)
                        },
                        onClick = {
                            onTaskSelected(task)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = "/",
            color = IconGray,
            fontSize = 30.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = selectedTask,
            color = DarkFont,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
private fun FocusMinutePicker(
    selectedMinutes: Int,
    onMinuteChanged: (Int) -> Unit,
    onDone: () -> Unit
) {
    val minutesOptions = listOf<Int?>(null) + (1..120).toList() + listOf<Int?>(null)
    val itemHeight = 46.dp
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedMinutes.coerceIn(1, 120) - 1
    )

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex + 1
        }
            .distinctUntilChanged()
            .collect { centerIndex ->
                val minute = minutesOptions.getOrNull(centerIndex)

                if (minute != null) {
                    onMinuteChanged(minute)
                }
            }
    }

    Box(
        modifier = Modifier
            .height(itemHeight * 3)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth(0.55f)
                .clip(RoundedCornerShape(18.dp))
                .background(BgSelectedElement.copy(alpha = 0.7f))
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .height(itemHeight * 3)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(minutesOptions) { index, minute ->
                val centerIndex = listState.firstVisibleItemIndex + 1
                val isSelected = index == centerIndex && minute != null

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable(enabled = minute != null) {
                            if (isSelected) {
                                onDone()
                            } else if (minute != null) {
                                onMinuteChanged(minute)
                                coroutineScope.launch {
                                    listState.animateScrollToItem((minute - 1).coerceAtLeast(0))
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (minute != null) {
                        Text(
                            text = "${minute}:00",
                            color = if (isSelected) {
                                DkGreen
                            } else {
                                IconGray.copy(alpha = 0.35f)
                            },
                            fontSize = if (isSelected) {
                                38.sp
                            } else {
                                24.sp
                            },
                            fontWeight = if (isSelected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusSubTasksCard(
    task: Task,
    onToggleSubTask: (SubTask) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.72f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Siguiente paso",
            color = DkGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        val visibleSubTasks = task.subTasks.sortedBy { it.completed }.take(4)
        visibleSubTasks.forEach { subTask ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BgUnselectedElement.copy(alpha = 0.82f))
                    .clickable { onToggleSubTask(subTask) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (subTask.completed) R.drawable.ic_checkbox_on else R.drawable.ic_checkbox
                    ),
                    contentDescription = if (subTask.completed) "Subtarea completada" else "Subtarea pendiente",
                    tint = IconGray,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = subTask.title,
                    color = DarkFont,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
        }

        if (task.subTasks.size > visibleSubTasks.size) {
            Text(
                text = "+${task.subTasks.size - visibleSubTasks.size} subtareas más",
                color = IconGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TimerIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    small: Boolean = false
) {
    val size = if (small) 48.dp else 72.dp

    Button(
        modifier = Modifier.size(size),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (small) {
                BgUnselectedElement
            } else {
                LgGreen
            },
            contentColor = if (small) {
                DkGreen
            } else {
                Color.White
            }
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(if (small) 22.dp else 30.dp)
        )
    }
}

@Composable
private fun MuteButton(
    isMuted: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.size(52.dp),
        onClick = onClick,
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isMuted) {
                LgGreen
            } else {
                BgUnselectedElement
            },
            contentColor = if (isMuted) {
                Color.White
            } else {
                DkGreen
            }
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isMuted) {
                    R.drawable.ic_sound_off
                } else {
                    R.drawable.ic_sound_on
                }
            ),
            contentDescription = if (isMuted) "Sonido desactivado" else "Sonido activado",
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun Int.toPomodoroTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun getBreakMinutesForFocus(focusMinutes: Int): Int {
    return when (focusMinutes) {
        in 1..15 -> 3
        in 16..25 -> 5
        in 26..40 -> 8
        in 41..60 -> 15
        in 61..90 -> 20
        else -> 25
    }
}