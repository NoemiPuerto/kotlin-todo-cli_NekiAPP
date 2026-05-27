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
import androidx.compose.foundation.layout.width
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
import com.neki.app.ui.theme.BgSelectedElement
import com.neki.app.ui.theme.DkGreen
import com.neki.app.ui.theme.IconGray
import com.neki.app.ui.theme.LgGreen

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
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BgSelectedElement
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_plus_box),
                contentDescription = "Agregar tarea",
                colorFilter = ColorFilter.tint(LgGreen),
                modifier = Modifier
                    .size(27.dp)
                    .clickable {
                        onExpandClick()
                    }
            )

            Spacer(modifier = Modifier.width(18.dp))

            BasicTextField(
                value = taskTitle,
                onValueChange = onTaskTitleChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = DkGreen,
                    fontSize = 17.sp,
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
                                color = IconGray.copy(alpha = 0.55f),
                                fontSize = 17.sp,
                                fontFamily = PixelFont,
                                fontWeight = FontWeight.Normal,
                                maxLines = 1
                            )
                        }

                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(width = 104.dp, height = 46.dp)
                    .background(
                        color = LgGreen,
                        shape = RoundedCornerShape(11.dp)
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
                    fontSize = 17.sp,
                    fontFamily = PixelFont,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }
    }
}