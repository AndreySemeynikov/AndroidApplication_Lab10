package com.example.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab.ui.theme.LabTheme

import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var topTextFieldValue by remember { mutableStateOf(TextFieldValue()) }
    var aValue by remember { mutableStateOf(TextFieldValue()) }
    var bValue by remember { mutableStateOf(TextFieldValue()) }
    var x3Value by remember { mutableStateOf(TextFieldValue()) }
    var x2Value by remember { mutableStateOf(TextFieldValue()) }
    var result by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val defaultBackgroundColor = MaterialTheme.colorScheme.background
    val secondaryColor = MaterialTheme.colorScheme.secondary
    var backgroundColor by remember { mutableStateOf(defaultBackgroundColor) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    backgroundColor = defaultBackgroundColor
                })
            },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Function evaluation \n" +
                    "y = a/x3 / (13x2 – b/7)",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                lineHeight = 35.sp
            ),
            color = if (backgroundColor == defaultBackgroundColor) Color.Black else Color.White
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Row {
                    RadioButton(
                        selected = backgroundColor == defaultBackgroundColor, onClick = {
                            backgroundColor = defaultBackgroundColor
                        },
                        colors = RadioButtonDefaults.colors(
                            if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.dark_purple) else colorResource(
                                id = R.color.white
                            )
                        )
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "White theme",
                        textAlign = TextAlign.Left,
                        color = if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.dark_purple) else colorResource(
                            id = R.color.white
                        )
                    )
                }
                Row {
                    RadioButton(selected = backgroundColor == secondaryColor, onClick = {
                        backgroundColor = secondaryColor
                    })
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically), text = "Dark theme",
                        color = if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.dark_purple) else colorResource(
                            id = R.color.white
                        )
                    )
                }
            }

            TextField(
                value = aValue,
                onValueChange = { aValue = it },
                label = { Text("Enter a") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = bValue,
                onValueChange = { bValue = it },
                label = { Text("Enter b") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = x3Value,
                onValueChange = { x3Value = it },
                label = { Text("Enter x3") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = x2Value,
                onValueChange = { x2Value = it },
                label = { Text("Enter x2") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.dark_purple) else colorResource(
                        id = R.color.white
                    ),
                    contentColor = if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.white) else colorResource(
                        id = R.color.dark_purple
                    )
                ),
                onClick = {
                    val a = aValue.text.toFloatOrNull()
                    val b = bValue.text.toFloatOrNull()
                    val x3 = x3Value.text.toFloatOrNull()
                    val x2 = x2Value.text.toFloatOrNull()

                    if (a == null || b == null || x3 == null || x2 == null) {
                        result = "Invalid input. Please enter numeric values."
                        return@Button
                    }

                    if (x3 == 0f || 13 * x2 - b / 7 == 0f) {
                        result = "Division by zero is not allowed."
                        return@Button
                    }

                    isLoading = true
                    result = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text("Calculate")
            }

            if (isLoading) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    val a = aValue.text.toFloatOrNull() ?: 0f
                    val b = bValue.text.toFloatOrNull() ?: 0f
                    val x3 = x3Value.text.toFloatOrNull() ?: 0f
                    val x2 = x2Value.text.toFloatOrNull() ?: 0f

                    val y = (a / x3) / (13 * x2 - b / 7)
                    result = "Result: %.2f".format(y)
                    isLoading = false
                }
                CircularProgressIndicator(
                    color = if (backgroundColor == defaultBackgroundColor) colorResource(id = R.color.dark_purple) else colorResource(
                        id = R.color.white
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .padding(top = 30.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                result?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (backgroundColor == defaultBackgroundColor) Color.Black else Color.White,
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculatorScreen() {
    LabTheme {
        CalculatorScreen()
    }
}
