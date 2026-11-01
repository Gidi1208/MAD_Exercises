package com.chris.neo.ex12

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class StudioTab(val title: String) {
    HABITS("Habits"),
    CANVAS("Draw Canvas"),
    TIMER("Focus Timer")
}

data class HabitItem(
    val id: Int,
    val title: String,
    val category: String,
    val isCompleted: Boolean = false
)

data class LinePath(
    val path: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun Timer() {
    var selectedTab by remember { mutableStateOf(StudioTab.HABITS) }
    var accentColor by remember { mutableStateOf(Color(0xFF6200EE)) }

    val colorOptions = listOf(
        Color(0xFF6200EE), // Indigo
        Color(0xFF00897B), // Teal
        Color(0xFFD81B60), // Pink
        Color(0xFFF57C00)  // Amber
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = accentColor,
            primaryContainer = accentColor.copy(alpha = 0.15f)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Habit & Focus Studio",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Experimental Compose Workspace",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Theme Accent Selector
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colorOptions.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { accentColor = color }
                            )
                        }
                    }
                }

                // Tab Navigation
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StudioTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }

                // Tab Content
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        StudioTab.HABITS -> HabitDashboardTab(accentColor = accentColor)
                        StudioTab.CANVAS -> InteractiveCanvasTab(accentColor = accentColor)
                        StudioTab.TIMER -> FocusTimerTab(accentColor = accentColor)
                    }
                }
            }
        }
    }
}

@Composable
fun HabitDashboardTab(accentColor: Color) {
    var habits by remember {
        mutableStateOf(
            listOf(
                HabitItem(1, "Morning Meditation & Breathing", "Mindfulness"),
                HabitItem(2, "30-min Outdoor Run / Cardio", "Fitness", isCompleted = true),
                HabitItem(3, "Read 20 Pages of a Book", "Productivity"),
                HabitItem(4, "Drink 2.5 Liters of Water", "Health", isCompleted = true)
            )
        )
    }

    var newHabitTitle by remember { mutableStateOf("") }
    val selectedCategory by remember { mutableStateOf("Productivity") }

    val completedCount = habits.count { it.isCompleted }
    val progressFraction = if (habits.isNotEmpty()) completedCount.toFloat() / habits.size else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "Progress")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Daily Progress Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Daily Goals Progress", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completedCount of ${habits.size} completed (${(animatedProgress * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = accentColor,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // Add Habit Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newHabitTitle,
                onValueChange = { newHabitTitle = it },
                label = { Text("New Habit Goal") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    if (newHabitTitle.isNotBlank()) {
                        val nextId = (habits.maxOfOrNull { it.id } ?: 0) + 1
                        habits = habits + HabitItem(nextId, newHabitTitle.trim(), selectedCategory)
                        newHabitTitle = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        // Habit List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits, key = { it.id }) { habit ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            habits = habits.map {
                                if (it.id == habit.id) it.copy(isCompleted = !it.isCompleted) else it
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (habit.isCompleted)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = habit.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (habit.isCompleted)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = habit.category,
                                style = MaterialTheme.typography.bodySmall,
                                color = accentColor
                            )
                        }

                        Checkbox(
                            checked = habit.isCompleted,
                            onCheckedChange = { checked ->
                                habits = habits.map {
                                    if (it.id == habit.id) it.copy(isCompleted = checked) else it
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InteractiveCanvasTab(accentColor: Color) {
    var lines by remember { mutableStateOf(listOf<LinePath>()) }
    var currentLine by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var drawColor by remember { mutableStateOf(accentColor) }
    var strokeWidth by remember { mutableFloatStateOf(8f) }

    val palette = listOf(
        accentColor,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Black
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Controls Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color Palette
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                palette.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(color)
                            .clickable { drawColor = color }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { if (lines.isNotEmpty()) lines = lines.dropLast(1) }) {
                    Text("Undo")
                }
                Button(onClick = { lines = emptyList(); currentLine = emptyList() }) {
                    Text("Clear")
                }
            }
        }

        // Brush Stroke Width Slider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Brush: ${strokeWidth.toInt()}px", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = strokeWidth,
                onValueChange = { strokeWidth = it },
                valueRange = 2f..30f,
                modifier = Modifier.weight(1f)
            )
        }

        // Canvas Surface
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F0F0))
                .pointerInput(drawColor, strokeWidth) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentLine = listOf(offset)
                        },
                        onDrag = { change, _ ->
                            currentLine = currentLine + change.position
                        },
                        onDragEnd = {
                            if (currentLine.isNotEmpty()) {
                                lines = lines + LinePath(currentLine, drawColor, strokeWidth)
                                currentLine = emptyList()
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Render finished lines
                lines.forEach { line ->
                    if (line.path.size > 1) {
                        val path = Path().apply {
                            moveTo(line.path.first().x, line.path.first().y)
                            for (i in 1 until line.path.size) {
                                lineTo(line.path[i].x, line.path[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = line.color,
                            style = Stroke(
                                width = line.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // Render in-progress current line
                if (currentLine.size > 1) {
                    val path = Path().apply {
                        moveTo(currentLine.first().x, currentLine.first().y)
                        for (i in 1 until currentLine.size) {
                            lineTo(currentLine[i].x, currentLine[i].y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = drawColor,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FocusTimerTab(accentColor: Color) {
    val totalTimeSeconds = 25 * 60 // 25 Minutes
    var remainingSeconds by remember { mutableIntStateOf(totalTimeSeconds) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else if (remainingSeconds == 0) {
            isRunning = false
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)
    val progress = remainingSeconds.toFloat() / totalTimeSeconds

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Focus Mode Timer",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Circular Timer Display
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 32.dp)
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = accentColor,
                strokeWidth = 12.dp,
                trackColor = accentColor.copy(alpha = 0.2f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    fontSize = 48.sp,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = if (isRunning) "Focusing..." else "Paused",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { isRunning = !isRunning },
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                modifier = Modifier.width(120.dp)
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            OutlinedButton(
                onClick = {
                    isRunning = false
                    remainingSeconds = totalTimeSeconds
                },
                modifier = Modifier.width(120.dp)
            ) {
                Text("Reset")
            }
        }
    }
}