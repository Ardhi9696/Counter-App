package com.lee.counterapp.ui

import androidx.lifecycle.ViewModel
// 🟩 AndroidX Lifecycle ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
// 🟥 Kotlin Coroutines Flow

class CounterViewModel : ViewModel() {
    // 🟩 ViewModel = holder state + aturan bisnis

    // =========================
    // Internal mutable state
    // =========================

    private val _uiState = MutableStateFlow(CounterUiState())
    // 🟥 MutableStateFlow
    // Menyimpan SELURUH state UI dalam satu object

    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()
    // 🟥 StateFlow read-only untuk UI
    // UI hanya boleh collect, tidak boleh mutate

    // =========================
    // Actions (dipanggil UI)
    // =========================

    fun decrement() {
        val state = _uiState.value
        // 🟥 Ambil snapshot state saat ini

        if (state.canDecrement) {
            _uiState.value = state.copy(
                counter = state.counter - 1
            )
            // 🟦 copy() = immutable update
            // Ini sangat penting untuk Compose & Flow
        }
    }

    /**
     * @return true jika increment berhasil
     * @return false jika sudah mencapai max
     */
    fun increment(): Boolean {
        val state = _uiState.value

        return if (!state.isMaxReached) {
            _uiState.value = state.copy(
                counter = state.counter + 1
            )
            true
        } else {
            false
        }
    }

    fun reset() {
        // Reset hanya mengubah counter
        _uiState.value = _uiState.value.copy(counter = 0)
    }
}
