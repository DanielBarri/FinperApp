package com.horrorcrux.finperapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horrorcrux.finperapp.R
import com.horrorcrux.finperapp.db.RecordsDatabase
import com.horrorcrux.finperapp.db.models.Record
import com.horrorcrux.finperapp.db.models.TransactionType
import com.horrorcrux.finperapp.repository.RecordsRepository
import com.horrorcrux.finperapp.states.AmountState
import com.horrorcrux.finperapp.states.CategoryState
import com.horrorcrux.finperapp.states.DescriptionState
import com.horrorcrux.finperapp.states.TransactionDateState
import com.horrorcrux.finperapp.states.TransactionTypeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class RecordViewModel(private val application: Application): ViewModel() {
    private val repository: RecordsRepository

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

    var recordToDelete by mutableStateOf<Int?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var currentId: Int? = null

    val isFormValid: Boolean
        get() = _transactionDate.value.transactionDate != null &&
                _category.value.category.isNotBlank() &&
                _description.value.description.isNotBlank() &&
                _amount.value.amount != 0.0

    private fun applyAmountSign(amount: Double, type: TransactionType): Double {
        return if (type == TransactionType.EXPENSE) {
            -abs(amount)
        } else {
            abs(amount)
        }
    }

    init {
        val db = RecordsDatabase.getInstance(application)
        val dao = db.recordsDao()
        repository = RecordsRepository(dao)
        all = repository.all()
    }

    private fun load(id: Int?){
        viewModelScope.launch {
            try {
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
                        transactionType = TransactionType.INCOME
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
            } catch (e: Exception) {
                Log.e("RecordViewModel", "Error loading record", e)
                _eventFlow.emit(Event.ShowError(application.getString(R.string.error_loading_record)))
            }
        }
    }

    fun onEvent(event: Event)  {
        when (event) {
            is Event.SetTransactionType -> {
                _transactionType.value = transactionType.value.copy(
                    transactionType = event.transactionType
                )
                // Adjust amount sign based on transaction type
                val currentAmount = _amount.value.amount
                val newAmount = applyAmountSign(currentAmount, event.transactionType)
                _amount.value = amount.value.copy(
                    amount = newAmount
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

            is Event.ShowDeleteConfirmation -> {
                recordToDelete = event.id
            }

            Event.CancelDelete -> {
                recordToDelete = null
            }

            Event.ConfirmDelete -> {
                recordToDelete?.let {
                    viewModelScope.launch {
                        isLoading = true
                        try {
                            repository.delete(it)
                            recordToDelete = null
                        } catch (e: Exception) {
                            Log.e("RecordViewModel", "Error deleting record", e)
                            recordToDelete = null
                            _eventFlow.emit(Event.ShowError(application.getString(R.string.error_deleting_record)))
                        } finally {
                            isLoading = false
                        }
                    }
                }
            }

            is Event.Load -> {
                load(event.id)
                openRecord = true
            }

            is Event.Save -> {
                Log.d("RecordViewModel", "Saving record - Type: ${_transactionType.value.transactionType}, Amount: ${_amount.value.amount}")

                // Only save if form is valid
                if (!isFormValid) return

                viewModelScope.launch {
                    isLoading = true
                    try {
                        if(currentId != null){
                            repository.update(Record(
                                id = currentId,
                                transactionDate = _transactionDate.value.transactionDate,
                                transactionType = _transactionType.value.transactionType,
                                category = _category.value.category,
                                description = _description.value.description,
                                amount = _amount.value.amount
                            ))
                        } else {
                            repository.insert(Record(
                                transactionDate = _transactionDate.value.transactionDate,
                                transactionType = _transactionType.value.transactionType,
                                category = _category.value.category,
                                description = _description.value.description,
                                amount = _amount.value.amount
                            ))
                        }
                        openRecord = false
                        _eventFlow.emit(Event.Save)
                    } catch (e: Exception) {
                        Log.e("RecordViewModel", "Error saving record", e)
                        _eventFlow.emit(Event.ShowError(application.getString(R.string.error_saving_record)))
                    } finally {
                        isLoading = false
                    }
                }
            }

            is Event.ShowError -> {
                // ShowError is only emitted, not handled here
            }
        }
    }
}