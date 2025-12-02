package com.horrorcrux.finperapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.horrorcrux.finperapp.components.DatePickerComponent
import com.horrorcrux.finperapp.db.models.Record
import com.horrorcrux.finperapp.ui.theme.FinperAppTheme
import com.horrorcrux.finperapp.viewmodels.Event
import java.util.Date

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
fun RecordDialog(openRecord: Boolean = true, transactionType: String ="Gasto", transactionDate: Date? = null, category: String = "Auto", description: String ="Gasolina", amount: Double = 1000.0, onEvent: (Event) -> Unit ={}) {
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
                                    onClick = { Event.SetCategory("ingreso")},
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(25.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0AB74C),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(fontSize = 20.sp,
                                        text = "Ingreso")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Button(
                                    onClick = {Event.SetCategory("gasto") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(25.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF94545),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(fontSize = 20.sp,
                                        text = "Gasto")
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Fecha"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            DatePickerComponent(modifier = Modifier.fillMaxWidth().height(50.dp),
                                selectedDate = transactionDate,
                                onDateSelected = { date ->
                                    onEvent(Event.SetTransactionDate(date))
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Categoría"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        color = Color(0x332C2C2C),
                                        shape = RoundedCornerShape(25.dp)
                                    ),
                                value = category,
                                onValueChange = {
                                    onEvent(Event.SetCategory(it))
                                },
                                placeholder = {
                                    Text(
                                        text = "Ingresa la categoría",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = Color.White.copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
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
                                    focusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                ),
                                shape = RoundedCornerShape(25.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Descripción"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        color = Color(0x332C2C2C),
                                        shape = RoundedCornerShape(25.dp)
                                    ),
                                value = description,
                                onValueChange = {
                                    onEvent(Event.SetDescription(it))
                                },
                                placeholder = {
                                    Text(
                                        text = "Ingresa la descripción",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = Color.White.copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
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
                                    focusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                ),
                                shape = RoundedCornerShape(25.dp)
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 20.sp,
                                text = "Monto"
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .background(
                                        color = Color(0x332C2C2C),
                                        shape = RoundedCornerShape(25.dp)
                                    ),
                                value = amount.toString(),
                                onValueChange = {
                                    onEvent(Event.SetAmount(it.toDouble()))
                                },
                                placeholder = {
                                    Text(
                                        text = "Ingresa el monto",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = Color.White.copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
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
                                    focusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color.White
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                ),
                                shape = RoundedCornerShape(25.dp)
                            )
                        }
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp), verticalArrangement = Arrangement.Bottom)
                        {
                            Button(
                                onClick = {onEvent(Event.Save)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(25.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0AB74C),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Text(fontSize = 20.sp,
                                    text = "Guardar")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {onEvent(Event.CloseRecord)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(25.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF94545),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Text(fontSize = 20.sp,
                                    text = "Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CrudScreen (
    allRecords: List<Record>,
    openRecord: Boolean,
    transactionType: String,
    transactionDate: Date,
    category: String,
    description: String,
    amount: Double,
    onEvent: (Event) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(20.dp)){
        LazyColumn() {
            items(allRecords){
                record ->
                ListItem(
                    headlineContent = {
                        Text(record.transactionDate.toString())},
                    supportingContent = {
                        Text(record.category)
                        Text(record.description)
                        Text(record.amount.toString())
                    },
                    trailingContent = {
                        IconButton(onClick = {onEvent(Event.Load(record.id))}) {
                            Icon(Icons.Rounded.Edit,
                                contentDescription = "Editar record: ${record.id}")
                        }
                        IconButton(onClick = {onEvent(Event.Delete(record.id))}) {
                            Icon(Icons.Rounded.Delete,
                                contentDescription = "Borrar record: ${record.id}")
                        }
                    }
                )
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