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
import com.horrorcrux.finperapp.states.AmountState
import com.horrorcrux.finperapp.states.CategoryState
import com.horrorcrux.finperapp.states.DescriptionState
import com.horrorcrux.finperapp.states.TransactionDateState
import com.horrorcrux.finperapp.states.TransactionTypeState
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

    private val _transactionType = mutableStateOf(TransactionTypeState())
    val transactionType: State<TransactionTypeState> = _transactionType

    private val _transactionDate = mutableStateOf(TransactionDateState())
    val transactionDate: State<TransactionDateState> = _transactionDate

    private val _category = mutableStateOf(CategoryState())
    val category: State<CategoryState> = _category

    private val _description = mutableStateOf(DescriptionState())
    val description: State<DescriptionState> = _description

    private val _amount = mutableStateOf(AmountState())
    val amount: State<AmountState> = _amount
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
            if (id != null) {
                repository.findById(id).also { record ->
                    currentId = record.id
                    _transactionType.value = transactionType.value.copy(
                        transactionType = record.transactionType
                    )
                    _transactionDate.value = transactionDate.value.copy(
                        transactionDate = record.transactionDate
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
                }
            } else {
                currentId = null
                _transactionType.value = transactionType.value.copy(
                    transactionType = ""
                )
                _transactionDate.value = transactionDate.value.copy(
                    transactionDate = null
                )
                _category.value = category.value.copy(
                    category = ""
                )
                _description.value = description.value.copy(
                    description = ""
                )
                _amount.value = amount.value.copy(
                    amount = 0.0
                )
            }
        }
    }

    fun onEvent(event: Event)  {
        when (event) {
            is Event.SetTransactionType -> {
                _transactionType.value = transactionType.value.copy(
                    transactionType = event.transactionType
                )
            }

            is Event.SetTransactionDate -> {
                _transactionDate.value = transactionDate.value.copy(
                    transactionDate = event.transactionDate
                )
            }

            is Event.SetCategory -> {
                _category.value = category.value.copy(
                    category = event.category
                )
            }

            is Event.SetDescription -> {
                _description.value = description.value.copy(
                    description = event.description
                )
            }

            is Event.SetAmount -> {
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
                        transactionDate = _transactionDate.value.transactionDate ?: Date(),
                        transactionType = _transactionType.value.transactionType,
                        category = _category.value.category,
                        description = _description.value.description,
                        amount = _amount.value.amount
                    ))
                } else {
                    repository.insert(Record(
                        transactionDate = _transactionDate.value.transactionDate ?: Date(),
                        transactionType = _transactionType.value.transactionType,
                        category = _category.value.category,
                        description = _description.value.description,
                        amount = _amount.value.amount
                    ))
                }
                openRecord = false

                coroutineScope.launch(Dispatchers.IO) {
                    _eventFlow.emit(Event.Save)
                }
            }
        }
    }
}