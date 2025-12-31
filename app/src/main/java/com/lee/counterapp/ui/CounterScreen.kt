package com.lee.counterapp.ui
// 🟦 Kotlin package
// Package ui = layer presentasi (Compose Screen + ViewModel)

import androidx.compose.foundation.layout.*
// 🟨 Jetpack Compose Foundation Layout
// Column, Row, Spacer, padding, size, fillMaxSize, dll

import androidx.compose.material3.*
// 🟧 Material Design 3
// Button, Text, Scaffold, SnackbarHost, MaterialTheme, dll

import androidx.compose.runtime.*
// 🟨 Compose Runtime
// State, remember, collectAsState, Composable, dll

import androidx.compose.runtime.saveable.rememberSaveable
// 🟨 rememberSaveable
// Menyimpan state kecil agar survive rotate / process recreation ringan

import androidx.compose.ui.Alignment
// 🟨 Alignment = posisi child di layout (Center, Start, End)

import androidx.compose.ui.Modifier
// 🟨 Modifier = cara Compose mengatur layout & styling

import androidx.compose.ui.text.font.FontWeight
// 🟨 Tebal tipografi

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// 🟨 Unit UI Compose
// dp = ukuran layout
// sp = ukuran teks (mengikuti accessibility)

import androidx.lifecycle.viewmodel.compose.viewModel
// 🟩 AndroidX Lifecycle + Compose
// viewModel() = helper untuk mengambil ViewModel
// Terikat lifecycle Activity/NavBackStack

import com.lee.counterapp.ui.components.ResetConfirmDialog
// 🟦 Import komponen UI reusable
// Dialog konfirmasi reset

import kotlinx.coroutines.launch
// 🟥 Kotlin Coroutine
// Digunakan untuk menjalankan side-effect (Snackbar)

// =====================================================
// Composable utama: CounterScreen
// =====================================================

@Composable
fun CounterScreen(
    vm: CounterViewModel = viewModel()
    // 🟩 ViewModel injection via Compose
    // Default: ViewModel dibuat & di-manage oleh lifecycle
) {

    // 🟨 collectAsState()
    // Mengubah StateFlow (dari ViewModel) → Compose State
    // Jika uiState berubah → UI otomatis recompose
    val state by vm.uiState.collectAsState()

    // 🟨 UI-only state
    // Mengontrol apakah dialog reset tampil atau tidak
    // Disimpan dengan rememberSaveable agar tidak hilang saat rotate
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    // 🟧 SnackbarHostState
    // Controller untuk menampilkan snackbar
    // remember() → tidak dibuat ulang saat recomposition
    val snackbarHostState = remember { SnackbarHostState() }

    // 🟥 Coroutine scope yang terikat lifecycle Composable
    // Dipakai untuk menjalankan showSnackbar()
    val scope = rememberCoroutineScope()

    // =================================================
    // Scaffold = kerangka UI Material
    // =================================================
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
        // 🟧 Slot khusus snackbar
        // Semua snackbar di screen ini akan lewat sini
    ) { padding ->
        // 🟨 padding = padding otomatis dari Scaffold
        // (status bar, snackbar, navigation bar)

        Column(
            modifier = Modifier
                .fillMaxSize()
                // 🟨 Mengisi seluruh layar

                .padding(padding)
                // 🟨 Padding dari Scaffold

                .padding(16.dp),
            // 🟨 Padding konten manual

            horizontalAlignment = Alignment.CenterHorizontally,
            // 🟨 Konten rata tengah horizontal

            verticalArrangement = Arrangement.Center
            // 🟨 Konten rata tengah vertikal
        ) {

            // ===== Judul App =====
            Text(
                text = "Counter App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
                // 🟧 Warna dari MaterialTheme
            )

            Spacer(Modifier.height(16.dp))
            // 🟨 Jarak vertikal

            // ===== Nilai Counter =====
            Text(
                text = "${state.counter}",
                // 🟦 String interpolation Kotlin
                // UI otomatis update saat state.counter berubah

                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            // ===== Tombol Aksi =====
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
                // 🟨 Jarak antar tombol
            ) {

                // ----- Tombol Minus -----
                Button(
                    onClick = { vm.decrement() },
                    // 🟩 Memanggil logic ViewModel
                    // Screen tidak memikirkan aturan minus

                    enabled = state.canDecrement,
                    // 🟨 UI declarative
                    // Enabled/disabled berdasarkan UiState

                    modifier = Modifier.size(80.dp)
                ) {
                    Text("-", fontSize = 32.sp)
                }

                // ----- Tombol Reset -----
                Button(
                    onClick = { showResetDialog = true },
                    // 🟨 Hanya mengubah UI state (dialog)
                    // Tidak langsung reset counter

                    enabled = state.canReset,
                    modifier = Modifier.height(80.dp)
                ) {
                    Text("Reset", fontSize = 18.sp)
                }

                // ----- Tombol Plus -----
                Button(
                    onClick = {
                        val ok = vm.increment()
                        // 🟩 Memanggil ViewModel
                        // Return Boolean = hasil logic

                        if (!ok) {
                            // 🟥 Side-effect UI
                            // Jika increment gagal → tampilkan Snackbar
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                // 🟧 Mencegah snackbar numpuk

                                snackbarHostState.showSnackbar(
                                    "Maximum count reached (${state.maxCount})"
                                )
                            }
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text("+", fontSize = 32.sp)
                }
            }
        }

        // =================================================
        // Dialog Reset (conditional rendering)
        // =================================================
        if (showResetDialog) {
            ResetConfirmDialog(
                onConfirm = {
                    vm.reset()
                    // 🟩 Logic reset ada di ViewModel

                    showResetDialog = false
                    // 🟨 Tutup dialog
                },
                onDismiss = {
                    showResetDialog = false
                    // 🟨 Tutup dialog tanpa ubah state counter
                }
            )
        }
    }
}
