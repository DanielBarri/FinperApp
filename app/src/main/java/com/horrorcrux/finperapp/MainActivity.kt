package com.horrorcrux.finperapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.horrorcrux.finperapp.ui.theme.FinperAppTheme
import com.horrorcrux.finperapp.viewmodels.Event

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinperAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

//Definir composables de mi app
@Preview(showBackground = true)
@Composable
fun RecordDialog(openRecord: Boolean = true, transactionType: String ="Gasto", category: String = "Auto", description: String ="Gasolina", amount: Double = 1000.0, onEvent: (Event) -> Unit ={}) {
    if(openRecord){
        Dialog(
            onDismissRequest = {
                onEvent(Event.CloseRecord)
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x80D092F3),
                        Color(0x75D092F3),
                        Color(0x6B64B7E3)
                    )
                )),
                ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x80D092F3),
                            Color(0x75D092F3),
                            Color(0x6B64B7E3)
                        )
                    ))
                ){
                    Box(modifier = Modifier
                        .padding(15.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.4f),  // Semi-transparent white
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.5f),  // Light border
                            shape = RoundedCornerShape(16.dp)
                        )){
                        Column(
                            modifier = Modifier.padding(40.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Tipo de Transacción"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(5.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0AB74C),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text(fontSize = 20.sp,
                                        text = "Ingreso")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(5.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF94545),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text(fontSize = 20.sp,
                                        text = "Gasto")
                                }
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Fecha"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(modifier = Modifier.fillMaxWidth()
                                .background(
                                    color = Color(0x332C2C2C),
                                    shape = RoundedCornerShape(5.dp)  // Rounds all corners
                                ),
                                value = transactionType,
                                onValueChange = {
                                    onEvent(Event.SetTransactionType(it))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Normal
                                ),
                                shape = RoundedCornerShape(5.dp)
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Categoría"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(modifier = Modifier.fillMaxWidth()
                                .background(
                                    color = Color(0x332C2C2C),
                                    shape = RoundedCornerShape(5.dp)  // Rounds all corners
                                ),
                                value = category,
                                onValueChange = {
                                    onEvent(Event.SetCategory(it))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Normal
                                ),
                                shape = RoundedCornerShape(5.dp)
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Descripción"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(modifier = Modifier.fillMaxWidth()
                                .background(
                                    color = Color(0x332C2C2C),
                                    shape = RoundedCornerShape(5.dp)  // Rounds all corners
                                ),
                                value = description,
                                onValueChange = {
                                    onEvent(Event.SetDescription(it))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Normal
                                ),
                                shape = RoundedCornerShape(5.dp)
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Monto"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(modifier = Modifier.fillMaxWidth()
                                .background(
                                color = Color(0x332C2C2C),
                                shape = RoundedCornerShape(5.dp)  // Rounds all corners
                                ),
                                value = amount.toString(),
                                onValueChange = {
                                    onEvent(Event.SetAmount(it.toDouble()))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Normal
                                ),
                                shape = RoundedCornerShape(5.dp)
                            )
                        }
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                            verticalAlignment = Alignment.Bottom)
                        { Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0AB74C),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(fontSize = 20.sp,
                                text = "Submit")
                        }}
                    }
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinperAppTheme {
        Greeting("Android")
    }
}