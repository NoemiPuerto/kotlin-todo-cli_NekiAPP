package com.neki.app.features.tasks.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neki.app.R

private val PixelFont = FontFamily(
    Font(R.font.pixelify_regular, FontWeight.Normal),
    Font(R.font.pixelify_medium, FontWeight.Medium),
    Font(R.font.pixelify_semibold, FontWeight.SemiBold),
    Font(R.font.pixelify_bold, FontWeight.Bold)
)

@Composable
fun QuickAddBar(
    taskTitle: String,
    onTaskTitleChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onExpandClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_plus_box),
                contentDescription = "Agregar tarea",
                colorFilter = ColorFilter.tint(Color(0xFF8B9D77)),
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        onExpandClick()
                    }
            )

            Spacer(modifier = Modifier.size(22.dp))

            BasicTextField(
                value = taskTitle,
                onValueChange = onTaskTitleChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color(0xFF777D72),
                    fontSize = 18.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (taskTitle.isBlank()) {
                            Text(
                                text = "Añadir nueva tarea",
                                color = Color(0xFFB5B8B1),
                                fontSize = 18.sp,
                                fontFamily = PixelFont,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.size(14.dp))

            Box(
                modifier = Modifier
                    .size(width = 116.dp, height = 52.dp)
                    .background(
                        color = Color(0xFF8B9D77),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        if (taskTitle.isNotBlank()) {
                            onCreateClick()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Crear",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}