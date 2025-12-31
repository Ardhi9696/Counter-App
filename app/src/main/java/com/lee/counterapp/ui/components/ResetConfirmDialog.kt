package com.lee.counterapp.ui.components
// 🟦 Kotlin package
// Folder components = tempat komponen UI kecil & reusable
// Biasanya dipakai lintas screen atau untuk merapikan Screen utama

import androidx.compose.material3.AlertDialog
// 🟧 Material Design 3
// AlertDialog = komponen dialog standar Material
// Sudah punya overlay, animasi, dan accessibility bawaan

import androidx.compose.material3.Button
// 🟧 Material Design 3 Button

import androidx.compose.material3.Text
// 🟧 Material Design 3 Text

import androidx.compose.runtime.Composable
// 🟨 Jetpack Compose annotation
// Menandai function ini sebagai UI declarative

@Composable
fun ResetConfirmDialog(
    onConfirm: () -> Unit,
    // 🟦 Kotlin function type (lambda)
    // Callback yang dipanggil saat user menekan tombol "Yes"
    // Biasanya di-handle oleh Screen / ViewModel

    onDismiss: () -> Unit
    // 🟦 Kotlin function type (lambda)
    // Callback saat dialog ditutup atau user menekan "Cancel"
) {
    // 🟨 Composable function
    // Fungsi ini TIDAK menyimpan state sendiri
    // Dia hanya menerima perintah dari luar (stateless component)

    AlertDialog(
        // 🟧 Material AlertDialog
        // Dialog ini muncul DI ATAS UI utama (modal)

        onDismissRequest = onDismiss,
        // 🟨 Dipanggil saat:
        // - user klik area di luar dialog
        // - tombol back ditekan
        // Best practice: selalu hubungkan ke onDismiss

        title = {
            // 🟨 Lambda Composable
            // Slot title dari AlertDialog
            Text("Reset Counter")
        },

        text = {
            // 🟨 Lambda Composable
            // Slot body / konten utama dialog
            Text("Are you sure you want to reset the counter?")
        },

        confirmButton = {
            // 🟨 Lambda Composable
            // Tombol aksi utama (positive action)

            Button(onClick = onConfirm) {
                // 🟧 Button Material
                // onConfirm dipanggil → biasanya reset state di ViewModel
                Text("Yes")
            }
        },

        dismissButton = {
            // 🟨 Lambda Composable
            // Tombol aksi sekunder (negative action)

            Button(onClick = onDismiss) {
                // 🟧 Button Material
                // Hanya menutup dialog, tidak mengubah state utama
                Text("Cancel")
            }
        }
    )
}
