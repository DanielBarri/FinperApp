package com.horrorcrux.finperapp

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.Icon
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.horrorcrux.finperapp.components.DatePickerComponent
import com.horrorcrux.finperapp.db.models.Record
import com.horrorcrux.finperapp.states.TransactionTypeState
import com.horrorcrux.finperapp.ui.theme.FinperAppTheme
import com.horrorcrux.finperapp.viewmodels.Event
import com.horrorcrux.finperapp.viewmodels.RecordViewModel
import com.horrorcrux.finperapp.viewmodels.RecordViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinperAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Crud()
                }
            }
        }
    }
}

//Definir composables de mi app
@Preview(showBackground = true)
@Composable
fun RecordDialog(openRecord: Boolean = true,
                 transactionType: String = "ingreso",
                 transactionDate: Date? = null,
                 category: String = "",
                 description: String ="",
                 amount: Double = 0.0,
                 onEvent: (Event) -> Unit ={}) {
    if(openRecord){
        Dialog(
            onDismissRequest = {onEvent(Event.CloseRecord)},
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
                                    onClick = {onEvent(Event.SetTransactionType("ingreso"))},
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(25.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0AB74C).copy(
                                            alpha = if (transactionType == "ingreso") 1f else 0.4f
                                        ),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text(fontSize = 20.sp,
                                        text = "Ingreso")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Button(
                                    onClick = {onEvent(Event.SetTransactionType("gasto")) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(25.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF94545).copy(
                                            alpha = if (transactionType == "gasto") 1f else 0.4f
                                        ),
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
                                            color = Color(0xFF1A1A2E).copy(alpha = 0.6f),
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
                                    focusedTextColor = Color(0xFF1A1A2E),
                                    unfocusedTextColor = Color(0xFF1A1A2E),
                                    cursorColor = Color(0xFF1A1A2E)
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1A1A2E),
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
                                            color = Color(0xFF1A1A2E).copy(alpha = 0.6f),
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
                                    focusedTextColor = Color(0xFF1A1A2E),
                                    unfocusedTextColor = Color(0xFF1A1A2E),
                                    cursorColor = Color(0xFF1A1A2E)
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1A1A2E),
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
                                value = if (amount == 0.0) "" else abs(amount).toString(),
                                onValueChange = {
                                    val inputAmount = it.toDoubleOrNull()?: 0.0
                                    val newAmount = if (transactionType == "gasto") {
                                        -abs(inputAmount)
                                    } else {
                                        abs(inputAmount)
                                    }
                                    onEvent(Event.SetAmount(newAmount))
                                },
                                placeholder = {
                                    Text(
                                        text = "Ingresa el monto",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            color = Color(0xFF1A1A2E).copy(alpha = 0.6f),
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    focusedTextColor = Color(0xFF1A1A2E),
                                    unfocusedTextColor = Color(0xFF1A1A2E),
                                    cursorColor = Color(0xFF1A1A2E)
                                ),
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color(0xFF1A1A2E),
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
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Text(fontSize = 20.sp,
                                    text = "Guardar")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = {onEvent(Event.CloseRecord)},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
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

@Preview(showBackground = true)
@Composable
fun CrudScreen (
    allRecords: List<Record> = listOf(
        Record(id= null,
            transactionDate = Date(),
            transactionType = "ingreso",
            category = "Auto",
            description = "Gasolina",
            amount = 100.00),
        Record(id= null,
            transactionDate = Date(),
            transactionType = "ingreso",
            category = "Auto",
            description = "Gasolina",
            amount = 100.00),
        Record(id= null,
            transactionDate = Date(),
            transactionType = "ingreso",
            category = "Auto",
            description = "Gasolina",
            amount = 100.00)
    ),
    openRecord: Boolean = false,
    transactionType: String = "ingreso",
    transactionDate: Date? = null,
    category: String = "",
    description: String = "",
    amount: Double = 0.0,
    onEvent: (Event) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x80D092F3),
                        Color(0x75D092F3),
                        Color(0x6B64B7E3)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .background(
                    color = Color.White.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allRecords) {
                        record ->
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    ListItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.5f)),
                        headlineContent = {
                            Text(record.transactionDate?.let { dateFormat.format(it) } ?: "No date")
                        },
                        supportingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "$${abs(record.amount)}",
                                    fontSize = 16.sp,
                                    color = if (record.transactionType == "ingreso") {
                                        Color(0xFF2E7D32)
                                    } else {
                                        Color.Red
                                    }
                                )
                                Text(record.category)
                                Text("|")
                                Text(record.description)
                            }
                        },
                        trailingContent = {
                            Row() {
                                IconButton(onClick = { onEvent(Event.Load(record.id)) }) {
                                    Icon(
                                        Icons.Rounded.Edit,
                                        contentDescription = "Editar record: ${record.id}"
                                    )
                                }
                                IconButton(onClick = { onEvent(Event.Delete(record.id)) }) {
                                    Icon(
                                        Icons.Rounded.Delete,
                                        contentDescription = "Borrar record: ${record.id}"
                                    )
                                }
                            }
                        })
                }
            }
        }
    }
    RecordDialog(
        openRecord = openRecord,
        transactionType = transactionType,
        transactionDate = transactionDate,
        category = category,
        description = description,
        amount = amount,
        onEvent = onEvent
    )
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CrudScreenSetup(
    viewModel: RecordViewModel){
    val allRecords by viewModel.all.observeAsState(listOf())

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarHostState) {
        viewModel.eventFlow.collectLatest {
            event ->
            when(event){
                Event.CloseRecord -> TODO()
                is Event.Delete ->  TODO()
                is Event.Load -> TODO()
                Event.OpenRecord -> TODO()
                Event.Save -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Nota guardada"
                        )
                    }
                }
                is Event.SetTransactionType -> TODO()
                is Event.SetTransactionDate -> TODO()
                is Event.SetCategory -> TODO()
                is Event.SetAmount -> TODO()
                is Event.SetDescription -> TODO()
            }
        }
    }
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(Event.Load(null))
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar record"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) {
            CrudScreen(
                allRecords = allRecords,
                openRecord = viewModel.openRecord,
                transactionType = viewModel.transactionType.value.transactionType,
                transactionDate = viewModel.transactionDate.value.transactionDate,
                category = viewModel.category.value.category,
                description = viewModel.description.value.description,
                amount = viewModel.amount.value.amount,
                onEvent = {event-> viewModel.onEvent(event)}
            )
        }
}

@Composable
fun Crud(){
    val owner = LocalViewModelStoreOwner.current
    owner?.let{
        val viewModel: RecordViewModel = viewModel(
            it,
            "RecordViewModel",
            RecordViewModelFactory(
                LocalContext.current.applicationContext as Application
            )
        )
        CrudScreenSetup(viewModel)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
