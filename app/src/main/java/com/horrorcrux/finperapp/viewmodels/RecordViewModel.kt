package com.horrorcrux.finperapp.viewmodels

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horrorcrux.finperapp.db.RecordsDatabase
import com.horrorcrux.finperapp.db.models.Record
import com.horrorcrux.finperapp.repository.RecordsRepository
import com.horrorcrux.finperapp.states.RecordFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Date

class RecordViewModel(application: Application): ViewModel() {
    private val repository: RecordsRepository

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _transactionType = mutableStateOf(RecordFieldState())
    val transactionType: State<RecordFieldState> = _transactionType

    private val _category = mutableStateOf(RecordFieldState())
    val category: State<RecordFieldState> = _category

    private val _description = mutableStateOf(RecordFieldState())
    val description: State<RecordFieldState> = _description

    private val _amount = mutableStateOf(RecordFieldState())
    val amount: State<RecordFieldState> = _amount

    //TODO: Pendiente resto de los formularios

    val all: LiveData<List<Record>>

    var openRecord by mutableStateOf(false)

    private var currentId: Int? = null

    init {
        val db = RecordsDatabase.getInstance(application)
        val dao = db.recordsDao()
        repository = RecordsRepository(dao)
        all = repository.all()
    }

    private fun load(id: Int?){
        viewModelScope.launch {
            if (id !== null) {
                repository.findById(id).also {
                    record ->
                    currentId = record.id
                    _transactionType.value = transactionType.value.copy(
                        transactionType = record.transactionType
                    )
                    _category.value = category.value.copy(
                        category = record.category
                    )
                    _description.value = description.value.copy(
                        description = record.description
                    )
                    _amount.value = amount.value.copy(
                        amount = record.amount
                    )
                    //TODO: traer nuevos campos
                }
            } else {
                currentId = null
                _transactionType.value = transactionType.value.copy(
                    transactionType = ""
                )
                _category.value = category.value.copy(
                    category = "Categoria"
                )
                _description.value = description.value.copy(
                    description = "Descripción"
                )
                _amount.value = amount.value.copy(
                    amount = 0.0
                )
            }
        }
    }

    fun onEvent(event: Event)  {
        when (event) {
            is Event.SetRecord -> {
               _transactionType.value = transactionType.value.copy(
                    transactionType = event.transactionType
               )
               _category.value = category.value.copy(
                   category = event.category
               )
               _description.value = description.value.copy(
                   description = event.description
               )
               _amount.value = amount.value.copy(
                   amount = event.amount
               )
            }

            is Event.CloseRecord -> {
                openRecord = false
            }

            is Event.OpenRecord -> {
                load(currentId)
                openRecord = true
            }

            is Event.Delete -> {
                event.id?.let {
                    repository.delete(it)
                }
            }

            is Event.Load -> {
                load(event.id)
                openRecord = true
            }

            is Event.Save -> {
                if(currentId != null){
                    repository.update(Record(
                        id = currentId,
                        transactionDate = Date(),
                        transactionType= transactionType.value.transactionType,
                        category = category.value.category,
                        description = description.value.description,
                        amount = amount.value.amount))
                } else {
                    repository.insert(Record(
                        transactionDate = Date(),
                        transactionType= transactionType.value.transactionType,
                        category = category.value.category,
                        description = description.value.description,
                        amount = amount.value.amount
                    ))
                }
                openRecord = false

                coroutineScope.launch(Dispatchers.IO) {
                    _eventFlow.emit(Event.Save)
                }

            }
            //TODO: Me falta agregar otros posibles campos
        }
    }
}
