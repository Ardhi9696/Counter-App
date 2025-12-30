package com.lee.counterapp // 🟦 Kotlin package
// Package = namespace / alamat unik aplikasi di Android
// Dipakai OS untuk identifikasi app (applicationId)

import android.os.Bundle // 🟩 Android API
// Bundle = container data kecil untuk lifecycle (onCreate, restore state)

import androidx.activity.ComponentActivity // 🟩 AndroidX Activity
// Activity modern yang kompatibel dengan Jetpack Compose

import androidx.activity.compose.setContent // 🟨 Bridge Android → Compose
// Mengganti setContentView(XML)

import androidx.activity.enableEdgeToEdge // 🟩 Android API
// Mengizinkan UI menggambar sampai system bar (status & navigation bar)


// =======================
// Layout (Jetpack Compose)
// =======================

import androidx.compose.foundation.layout.Arrangement // 🟨 Mengatur posisi child
import androidx.compose.foundation.layout.Column // 🟨 Layout vertikal
import androidx.compose.foundation.layout.Row // 🟨 Layout horizontal
import androidx.compose.foundation.layout.Spacer // 🟨 Ruang kosong
import androidx.compose.foundation.layout.fillMaxSize // 🟨 Modifier ukuran layar penuh
import androidx.compose.foundation.layout.height // 🟨 Modifier tinggi
import androidx.compose.foundation.layout.padding // 🟨 Modifier padding
import androidx.compose.foundation.layout.size // 🟨 Modifier ukuran tetap
import androidx.compose.foundation.layout.width // 🟨 Modifier lebar
import androidx.compose.foundation.shape.RoundedCornerShape // 🟨 Bentuk sudut membulat


// =======================
// Material Design 3
// =======================

import androidx.compose.material3.Button // 🟧 Komponen tombol Material
import androidx.compose.material3.MaterialTheme // 🟧 Akses warna, font, shape global
import androidx.compose.material3.Surface // 🟧 Container dasar Material
import androidx.compose.material3.Text // 🟧 Komponen teks Material


// =======================
// Compose Runtime (STATE)
// =======================

import androidx.compose.runtime.Composable // 🟨 Menandai function UI Compose
import androidx.compose.runtime.getValue // 🟦 Kotlin delegation (by)
import androidx.compose.runtime.mutableIntStateOf // 🟨 State Int yang efisien
import androidx.compose.runtime.saveable.rememberSaveable // 🟨 State survive rotate
import androidx.compose.runtime.setValue // 🟦 Kotlin delegation (by)


// =======================
// UI Primitive
// =======================

import androidx.compose.ui.Alignment // 🟨 Alignment layout
import androidx.compose.ui.Modifier // 🟨 Styling & layout Compose
import androidx.compose.ui.text.font.FontWeight // 🟨 Tebal tipografi
import androidx.compose.ui.unit.dp // 🟨 Density-independent pixel
import androidx.compose.ui.unit.sp // 🟨 Scale-independent pixel (text)


// =======================
// Theme aplikasi
// =======================

import com.lee.counterapp.ui.theme.CounterAppTheme // 🟧 Theme custom app


// =======================
// Logging & Side Effect
// =======================

import android.util.Log // 🟩 Android logging (Logcat)
import androidx.compose.material3.Scaffold // 🟧 Layout Material dengan slot snackbar
import androidx.compose.material3.SnackbarHost // 🟧 Host Snackbar
import androidx.compose.material3.SnackbarHostState // 🟧 Controller Snackbar
import androidx.compose.runtime.remember // 🟨 Menyimpan object selama composition
import androidx.compose.runtime.rememberCoroutineScope // 🟥 Coroutine scope untuk UI
import kotlinx.coroutines.launch // 🟥 Coroutine builder


// =======================
// Activity utama aplikasi
// =======================

class MainActivity : ComponentActivity() {
    // 🟩 Android Activity = entry point aplikasi

    override fun onCreate(savedInstanceState: Bundle?) {
        // 🟩 Lifecycle callback Android
        // Dipanggil saat Activity dibuat

        super.onCreate(savedInstanceState)
        // 🟩 Wajib: inisialisasi Activity oleh Android

        enableEdgeToEdge()
        // 🟩 Mengizinkan UI menggambar sampai edge layar

        setContent {
            // 🟨 Entry point Jetpack Compose
            // Semua UI Compose dimulai dari sini

            CounterAppTheme {
                // 🟧 Menerapkan MaterialTheme ke seluruh UI

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // 🟨 Modifier: Surface menutup seluruh layar

                    color = MaterialTheme.colorScheme.background
                    // 🟧 Background diambil dari theme
                ) {
                    CounterScreen()
                    // 🟨 Memanggil Composable buatan kita
                }
            }
        }
    }
}


// =======================
// Composable utama layar counter
// =======================

@Composable
fun CounterScreen() {
    // 🟨 Composable = function UI deklaratif

    val maxCount = 10
    // 🟦 Local variable Kotlin
    // Batas maksimum counter

    var counter by rememberSaveable { mutableIntStateOf(0) }
    // 🟨 STATE Compose
    // - rememberSaveable → survive rotate
    // - mutableIntStateOf → state Int efisien
    // - by → Kotlin delegation (tidak perlu .value)

    val snackbarHostState = remember { SnackbarHostState() }
    // 🟧 Controller Snackbar
    // remember → tidak dibuat ulang saat recomposition

    val scope = rememberCoroutineScope()
    // 🟥 Coroutine scope yang terikat lifecycle Composable
    // Digunakan untuk side-effect (Snackbar)


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
        // 🟧 Scaffold = layout Material utama
        // Snackbar ditempatkan di slot snackbarHost
    ) { padding ->
        // 🟨 padding = padding otomatis dari Scaffold (system bar, snackbar)

        Column(
            modifier = Modifier
                .fillMaxSize()
                // 🟨 Column memenuhi layar

                .padding(padding)
                // 🟨 Padding dari Scaffold

                .padding(16.dp),
            // 🟨 Padding manual konten

            horizontalAlignment = Alignment.CenterHorizontally,
            // 🟨 Tengah horizontal

            verticalArrangement = Arrangement.Center
            // 🟨 Tengah vertikal
        ) {

            // ===== Title =====
            Text(
                text = "Counter App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
                // 🟧 Warna dari theme
            )

            Spacer(modifier = Modifier.height(16.dp))
            // 🟨 Jarak vertikal

            // ===== Counter Value =====
            Text(
                text = "$counter",
                // 🟦 String interpolation Kotlin
                // UI otomatis update saat counter berubah

                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))


            // ===== Tombol =====
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
                // 🟨 Jarak antar tombol
            ) {

                // ----- Button Minus -----
                Button(
                    shape = RoundedCornerShape(8.dp),
                    // 🟧 Bentuk tombol

                    enabled = counter > 0,
                    // 🟨 UI state declarative
                    // Tombol mati saat counter = 0

                    onClick = {
                        counter--
                        // 🟦 Logic Kotlin

                        Log.d(
                            "CounterApp",
                            "Button - Clicked, counter = $counter"
                        )
                        // 🟩 Logcat Android
                    },

                    modifier = Modifier.size(80.dp)
                    // 🟨 Ukuran tombol
                ) {
                    Text("-", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))


                // ----- Button Reset -----
                Button(
                    shape = RoundedCornerShape(8.dp),

                    onClick = {
                        counter = 0
                        // 🟦 Reset state

                        Log.d(
                            "CounterApp",
                            "Button Reset Clicked, counter = 0"
                        )
                    },

                    enabled = counter != 0,
                    // 🟨 Tombol reset aktif hanya jika counter ≠ 0

                    modifier = Modifier.height(80.dp)
                ) {
                    Text("Reset", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))


                // ----- Button Plus -----
                Button(
                    shape = RoundedCornerShape(8.dp),

                    onClick = {
                        if (counter < maxCount) {
                            counter++
                            // 🟦 Increment biasa
                        } else {
                            scope.launch {
                                // 🟥 Coroutine UI
                                snackbarHostState.showSnackbar(
                                    message = "Maximum count reached($maxCount)"
                                )
                                // 🟧 Snackbar = side-effect UI
                            }
                        }

                        Log.d(
                            "CounterApp",
                            "Button + Clicked, counter = $counter"
                        )
                    },

                    modifier = Modifier.size(80.dp)
                ) {
                    Text("+", fontSize = 32.sp)
                }
            }
        }
    }
}
