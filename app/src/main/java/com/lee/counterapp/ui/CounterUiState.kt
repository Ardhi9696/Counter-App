package com.lee.counterapp.ui
// 🟦 Kotlin package

/**
 * CounterUiState
 *
 * 🟨 UiState = representasi SATU SUMBER KEBENARAN untuk UI
 * Semua data yang dibutuhkan layar dikumpulkan di sini
 *
 * Prinsip penting Compose:
 * UI = fungsi dari state
 */
data class CounterUiState(
    val counter: Int = 0,
    val maxCount: Int = 10
) {
    // 🟦 Kotlin data class
    // data class otomatis membuat:
    // - equals()
    // - hashCode()
    // - toString()
    // - copy()

    // =========================
    // Derived state (helper UI)
    // =========================

    val canDecrement: Boolean
        get() = counter > 0
    // 🟦 Properti turunan
    // UI tidak perlu hitung ulang counter > 0

    val canReset: Boolean
        get() = counter != 0
    // 🟦 Properti turunan
    // UI tinggal pakai state.canReset

    val isMaxReached: Boolean
        get() = counter >= maxCount
    // 🟦 Properti turunan
    // Berguna untuk snackbar / disable tombol
}
